package com.sprint.mission.findex.syncjob.service;

import com.sprint.mission.findex.syncjob.dto.request.SyncJobCreateRequestDto;
import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.mapper.SyncJobCursorPageResponseMapper;
import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import com.sprint.mission.findex.autosyncconfig.repository.AutoSyncConfigRepository;
import com.sprint.mission.findex.client.FindexOpenApiClient;
import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import com.sprint.mission.findex.indexinfo.SourceType;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.mapper.SyncJobCursorPageResponseMapper;
import com.sprint.mission.findex.syncjob.mapper.SyncJobMapper;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncJobRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.StreamSupport;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncJobService {
    private final SyncJobRepository syncRepository;

    private final SyncJobMapper syncJobMapper;
    private final SyncJobCursorPageResponseMapper cursorPageResponseMapper;
    private final FindexOpenApiClient findexOpenApiClient;
    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;
    /*
        연동 결과 생성 및 반환
     */
    public List<SyncJobDto> createSyncJob(SyncJobCreateRequestDto syncJobCreateRequestDto, String clientIp) {
        // 1. 제약 조건: 날짜 유효성 검사 (시작일이 종료일보다 뒤면 에러)
        if (syncJobCreateRequestDto.baseDateFrom().isAfter(syncJobCreateRequestDto.baseDateTo())) {
            throw new IllegalArgumentException("시작 날짜가 종료 날짜보다 미래일 수 없습니다.");
        }

        // 2. 작업자 정보 결정: IP가 있으면 IP 저장, 없으면 배치가 실행한 'system'으로 저장
        String worker =
            (syncJobCreateRequestDto.worker() == null || syncJobCreateRequestDto.worker().isBlank())
                ? "system"
                : syncJobCreateRequestDto.worker();

        // 3. 루프 안에서 매번 save 하지 않고 리스트에 모아서 한 번에 저장
        List<SyncJob> syncJobsToSave = new ArrayList<>();

        // 4. 대상 지수가 여러 개인 경우 지수별로 반복 처리 (Outer Loop)
        for (Long indexId : syncJobCreateRequestDto.indexInfoIds()) {

            // 대상 날짜가 여러 개인 경우 날짜별로 반복 처리 (Inner Loop)
            LocalDate currentDate = syncJobCreateRequestDto.baseDateFrom();

            while (!currentDate.isAfter(syncJobCreateRequestDto.baseDateTo())) {

                // 엔티티 생성: 넘겨받은 연동 결과와 식별된 정보를 조합
                SyncJob syncJob = SyncJob.builder()
                    .jobType(JobType.valueOf(syncJobCreateRequestDto.jobType()))    // 문자열 타입을 Enum으로 변환
                    .targetDate(currentDate)                                        // 루프 중인 현재 날짜 설정
                    .worker(worker)                                                 // 추출된 작업자 정보 설정
                    .jobTime(LocalDateTime.now())                                   // 현재 작업 일시 기록
                    .result(JobResult.SUCCESS)                                      // 기본적으로 성공으로 기록
                    // .indexInfo(indexInfoRepository.getReferenceById(indexId))    // 지수 엔티티 연결(준비 시 주석 해제)
                    .build();

                // 저장용 리스트에 추가
                syncJobsToSave.add(syncJob);

                // 다음 날짜로 이동
                currentDate = currentDate.plusDays(1);
            }
        }

        // 5. DB에 일괄 저장
        Iterable<SyncJob> savedEntities = syncRepository.saveAll(syncJobsToSave);

        // 6. 저장된 결과를 DTO 리스트로 변환하여 반환
        return StreamSupport.stream(savedEntities.spliterator(), false)
            .map(SyncJobDto::from)
            .toList();
    }

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
    public List<SyncJobDto> syncIndexInfos(String worker) {
        // 오늘 날짜를 API 요청용 형식(yyyyMMdd)으로 변환
        LocalDate today = LocalDate.now();
        String baseDate = today.format(DateTimeFormatter.BASIC_ISO_DATE);

        // 생성된 연동 이력을 저장할 리스트
        List<SyncJob> syncJobs = new ArrayList<>();

        // 외부 API 호출 및 응답 수신
        Mono<StockMarketIndexResponseDto> apiResponses = findexOpenApiClient.fetchStockIndexInfo( "20200102"); // TODO: 현재는 테스트를 위해 20200102 날짜를 넣음
        StockMarketIndexResponseDto response = apiResponses.block();

        // 응답이 비정상이면 빈 리스트 반환
        if (response == null
                || response.response() == null
                || response.response().body() == null
                || response.response().body().items() == null
                || response.response().body().items().item() == null
                || response.response().body().items().item().isEmpty()) {
            return List.of();
        }

        // 외부 API를 통해 불러온 응답 중 item 만 가져옴
        List<StockMarketIndexResponseDto.IndexItem> items = response.response().body().items().item();

        // 지수별로 개별 처리 후, 지수별로 연동 이력 생성
        for (StockMarketIndexResponseDto.IndexItem item : items) {
            // 연동 이력의 대상 날짜
            LocalDate targetDate = LocalDate.parse(item.baseDate(), DateTimeFormatter.BASIC_ISO_DATE);

            // 지수 정보의 기준 시점
            LocalDate basePointInTime = LocalDate.parse(item.basePointTime(), DateTimeFormatter.BASIC_ISO_DATE);
            IndexInfo savedIndexInfo = null;
            try {
                // 동일 지수 존재 여부 확인
                boolean isExist = indexInfoRepository.existsByIndexClassificationAndIndexName(
                        item.indexClassification(),
                        item.indexName()
                );

                if (isExist) {
                    // 기존 지수 정보 조회
                    savedIndexInfo = indexInfoRepository.findByIndexClassificationAndIndexName(
                            item.indexClassification(),
                            item.indexName()
                    );

                    // 기존 즐겨찾기 값은 유지하면서 Open API 기준으로 지수 정보 수정
                    IndexInfoUpdateRequestDto request = new IndexInfoUpdateRequestDto(
                            item.employedItemsCount(),
                            basePointInTime,
                            BigDecimal.valueOf(item.baseIndex()),
                            savedIndexInfo.isFavorite()
                    );

                    savedIndexInfo.update(request);
                    savedIndexInfo.updateSourceType(SourceType.OPEN_API);
                    indexInfoRepository.save(savedIndexInfo);

                } else {
                    // 기존 지수가 없으면 신규 생성
                    savedIndexInfo = indexInfoRepository.save(
                            IndexInfo.builder()
                                    .indexClassification(item.indexClassification())
                                    .indexName(item.indexName())
                                    .employedItemsCount(item.employedItemsCount())
                                    .basePointInTime(basePointInTime)
                                    .baseIndex(BigDecimal.valueOf(item.baseIndex()))
                                    .sourceType(SourceType.OPEN_API)
                                    .favorite(false)
                                    .build()
                    );

                    // 신규 지수에 대한 자동 연동 설정 생성
                    AutoSyncConfig autoSyncConfig = AutoSyncConfig.builder()
                            .indexInfo(savedIndexInfo)
                            .enabled(false)
                            .build();
                    autoSyncConfigRepository.save(autoSyncConfig);
                }

                // 해당 지수 연동 성공 이력 저장
                SyncJob successSyncJob = SyncJob.builder()
                        .jobType(JobType.INDEX_INFO)
                        .indexInfo(savedIndexInfo)
                        .targetDate(targetDate)
                        .worker(worker)
                        .jobTime(LocalDateTime.now())
                        .result(JobResult.SUCCESS)
                        .build();

                syncRepository.save(successSyncJob);
                syncJobs.add(successSyncJob);

            } catch (Exception e) {
                // 특정 지수 처리 실패 시 실패 이력 저장 후 다음 지수 계속 진행
                SyncJob failureSyncJob = SyncJob.builder()
                        .jobType(JobType.INDEX_INFO)
                        .indexInfo(savedIndexInfo)
                        .targetDate(targetDate)
                        .worker(worker)
                        .jobTime(LocalDateTime.now())
                        .result(JobResult.FAILURE)
                        .build();

                syncRepository.save(failureSyncJob);
                syncJobs.add(failureSyncJob);
            }
        }

        // 저장된 연동 이력을 DTO로 변환하여 반환
        return syncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();
    }
}
