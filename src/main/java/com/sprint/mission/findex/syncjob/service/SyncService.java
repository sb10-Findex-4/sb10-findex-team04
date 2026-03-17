package com.sprint.mission.findex.syncjob.service;

import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import com.sprint.mission.findex.autosyncconfig.repository.AutoSyncConfigRepository;
import com.sprint.mission.findex.client.FindexOpenApiClient;
import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import com.sprint.mission.findex.indexinfo.SourceType;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.syncjob.mapper.CursorPageResponseMapper;
import com.sprint.mission.findex.syncjob.mapper.SyncJobMapper;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncService {
    private final SyncRepository syncRepository;

    private final SyncJobMapper syncJobMapper;
    private final CursorPageResponseMapper cursorPageResponseMapper;
    private final FindexOpenApiClient findexOpenApiClient;
    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;

    /*
        연동 작업 목록 조회
     */
    public CursorPageResponseSyncJobDto<SyncJobDto> findAllSyncJobs(SyncJobSearchConditionDto syncJobSearchConditionDto) {
        // 1. Dto에 담긴 정렬 방향을 정렬 방식으로 설정 (기본값: DESC)
        Sort sort = syncJobSearchConditionDto.sortDirection().equalsIgnoreCase("desc")
                ? Sort.by(syncJobSearchConditionDto.sortField()).descending()
                : Sort.by(syncJobSearchConditionDto.sortField()).ascending();

        // 2. 다음 페이지 유무 확인을 위해 가져올 페이지 개수를 기본 size보다 하나 더 큰 크기로 설정
        Pageable limit = PageRequest.of(0, syncJobSearchConditionDto.size() + 1, sort);

        List<SyncJob> syncJobs;                                 // 레파지토리로부터 가져올 연동 작업(SyncJob) 저장 리스트
        Long idAfter = syncJobSearchConditionDto.idAfter();     // Dto로부터 받은 다음 페이지 시작점

        // 3. 이전 페이지 유무에 따른 분기 설정
        if (idAfter == null) {
            // 첫 페이지: 필터 조건들만 적용하여 조회
            syncJobs = syncRepository.findFirstPageSyncJobs(syncJobSearchConditionDto, limit);
        } else {
            // 다음 페이지: 특정 ID 이후 조건까지 포함하여 조회
            syncJobs = syncRepository.findNextPageSyncJobsById(syncJobSearchConditionDto, idAfter, limit);
        }

        // 4. 다음 페이지 유무 확인 | 11개를 가져왔다면 다음 페이지가 존재하는 것
        boolean hasNext = syncJobs.size() > syncJobSearchConditionDto.size();
        List<SyncJob> pagedSyncJobs = hasNext
                                      ? syncJobs.subList(0, syncJobSearchConditionDto.size())   // 있다면, 기본 페이지 크기 개수(10개)만큼 자르기
                                      : syncJobs;                                               // 없다면, 그대로 전달

        // 5. 이전 페이지의 마지막 요소 ID 설정 = 다음 요청의 idAfter
        Long nextIdAfter = (hasNext && !pagedSyncJobs.isEmpty())
                ? pagedSyncJobs.get(pagedSyncJobs.size() - 1).getId()
                : null;

        // 6. 다음 페이지 시작점(cursor) 지정 = 다음 페이지의 cursor
        String nextCursor = (hasNext && !pagedSyncJobs.isEmpty())
                ? pagedSyncJobs.get(pagedSyncJobs.size() - 1).getJobTime().toString()
                : null;

        // 7. 연동 작업 전체 개수 저장
        long totalElements = syncRepository.countWithFilter(syncJobSearchConditionDto);

        // 8. 연동 작업 (SyncJob) 엔티티 -> 응답 DTO 변환
        List<SyncJobDto> content = pagedSyncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();

        // 8. 응답 DTO -> 페이징 응답 DTO 반환
        return cursorPageResponseMapper.fromCursor(content, nextCursor, nextIdAfter, content.size(), totalElements, hasNext);
    }

    /*
        지수 정보 API 연동
     */
    @Transactional
    public void syncIndexInfos() {
        // 현재 날짜 생성
        LocalDateTime today = LocalDateTime.now();
        String baseDate = today.format(DateTimeFormatter.BASIC_ISO_DATE);

        try {
            // 외부 API 호출 및 responseDto로 변환
            Mono<StockMarketIndexResponseDto> apiResponses = findexOpenApiClient.fetchStockIndexInfo(baseDate);
            StockMarketIndexResponseDto response = apiResponses.block();

            List<StockMarketIndexResponseDto.IndexItem> items = response.response().body().items().item();

            if (items == null) {
                // 연동 작업 실패인가 과연 ??
                return;
            }

            // 중복 존재 유무 검사
            for (StockMarketIndexResponseDto.IndexItem item : items) {
                boolean isExist = indexInfoRepository.existsByIndexClassificationAndIndexName(
                        item.indexClassification(),
                        item.indexName()
                );

                if (isExist) { // 중복된 게 있을 경우는 수정
                    IndexInfo indexInfo = indexInfoRepository.findByIndexClassificationAndIndexName(
                            item.indexClassification(),
                            item.indexName()
                    );

                    // 지수 정보 수정
                    IndexInfoUpdateRequestDto request = new IndexInfoUpdateRequestDto(
                            item.employedItemsCount(),
                            LocalDate.parse(item.baseDate(), DateTimeFormatter.BASIC_ISO_DATE),
                            BigDecimal.valueOf(item.baseIndex()),
                            indexInfo.isFavorite()
                    );
                    indexInfo.update(request);
                    indexInfoRepository.save(indexInfo);

                } else { // 중복 없다면 새롭게 생성
                    // indexInfo 생성
                    IndexInfo newIndexInfo = indexInfoRepository.save(
                            IndexInfo.builder()
                                    .indexClassification(item.indexClassification())
                                    .indexName(item.indexName())
                                    .employedItemsCount(item.employedItemsCount())
                                    .basePointInTime(LocalDate.parse(item.baseDate(), DateTimeFormatter.BASIC_ISO_DATE))
                                    .baseIndex(BigDecimal.valueOf(item.baseIndex()))
                                    .sourceType(SourceType.OPEN_API)
                                    .favorite(false)
                                    .build()
                    );

                    // 자동 연동 설정 정보 생성
                    AutoSyncConfig autoSyncConfig = AutoSyncConfig.builder()
                            .indexInfo(newIndexInfo)
                            .enabled(false)
                            .build();
                    autoSyncConfigRepository.save(autoSyncConfig);

                    // 연동 이력 생성
                }
            }
        }catch (Exception e) {
            // 연동 이력 생성 , 실패로
        }

    }
}
