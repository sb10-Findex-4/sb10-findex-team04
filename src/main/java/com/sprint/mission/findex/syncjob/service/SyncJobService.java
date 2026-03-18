package com.sprint.mission.findex.syncjob.service;

import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.repository.IndexDataRepository;
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
import com.sprint.mission.findex.syncjob.mapper.SyncJobMapper;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncJobRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncJobService {
    private final SyncJobRepository syncJobRepository;

    private final SyncJobMapper syncJobMapper;
    private final SyncJobCursorPageResponseMapper syncJobCursorPageResponseMapper;
    private final FindexOpenApiClient findexOpenApiClient;
    private final IndexInfoRepository indexInfoRepository;
    private final IndexDataRepository indexDataRepository;
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
            IndexInfo indexInfo = indexInfoRepository.getReferenceById(indexId);

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
                    .indexInfo(indexInfo)                                           // 지수 엔티티 연결
                    .build();

                // 저장용 리스트에 추가
                syncJobsToSave.add(syncJob);

                // 다음 날짜로 이동
                currentDate = currentDate.plusDays(1);
            }
        }

        // 5. DB에 일괄 저장
        Iterable<SyncJob> savedEntities = syncJobRepository.saveAll(syncJobsToSave);

        // 6. 저장된 결과를 DTO 리스트로 변환하여 반환
        return StreamSupport.stream(savedEntities.spliterator(), false)
            .map(SyncJobDto::from)
            .toList();
    }

    /*
        연동 작업 목록 조회
     */
    public CursorPageResponseSyncJobDto<SyncJobDto> findAllSyncJobs(SyncJobSearchConditionDto condition) {
        // 1. 필터링 + 정렬 + 커서 기반 페이지네이션이 적용된 연동 작업 리스트
        List<SyncJob> syncJobs = syncJobRepository.searchSyncJobs(condition);

        // 2. 다음 페이지 유무 확인 | 11개를 가져왔다면 다음 페이지가 존재하는 것
        boolean hasNext = syncJobs.size() > condition.size();
        List<SyncJob> pagedSyncJobs = hasNext
                                      ? syncJobs.subList(0, condition.size())       // 있다면, 기본 페이지 크기 개수(10개)만큼 자르기
                                      : syncJobs;                                   // 없다면, 그대로 전달

        // 3. 이전 페이지의 마지막 요소 ID (다음 요청의 idAfter) 및 다음 페이지 시작점 (다음 페이지의 cursor) 설정
        Long nextIdAfter = null;
        String nextCursor = null;

        if (!pagedSyncJobs.isEmpty()) {
            SyncJob lastItem = pagedSyncJobs.get(pagedSyncJobs.size() - 1);

            // 다음 페이지가 존재할 때만 커서 및 마지막 요소 ID 전달
            if (hasNext) {
                nextIdAfter = lastItem.getId();
                nextCursor = "jobTime".equals(condition.sortField())
                        ? lastItem.getJobTime().toString()
                        : lastItem.getTargetDate().toString();
            }
        }

        // 7. 연동 작업 전체 개수 저장
        long totalElements = syncJobRepository.countWithFilter(condition);

        // 8. 연동 작업 (SyncJob) 엔티티 -> 응답 DTO 변환
        List<SyncJobDto> content = pagedSyncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();

        // 8. 응답 DTO -> 페이징 응답 DTO 반환
        return syncJobCursorPageResponseMapper.fromCursor(
                content,
                nextCursor,
                nextIdAfter,
                content.size(),
                totalElements,
                hasNext);
    }

    /*
    지수 정보 연동 Api
     */
    @Transactional
    public List<SyncJobDto> syncIndexInfos(String worker) {
        // 오늘 날짜를 yyyyMMdd 형식으로 변환 (API 요청용)
        LocalDate today = LocalDate.now();
        String baseDate = today.format(DateTimeFormatter.BASIC_ISO_DATE);

        // 외부 API 호출 후 동기적으로 응답 받음 (.block() = 응답 올 때까지 대기)
        StockMarketIndexResponseDto response = findexOpenApiClient
                .fetchStockIndexInfo(baseDate).block();

        // 응답이 null이거나 내부 데이터가 없으면 빈 리스트 반환 (빈 리스트 이슈 처리)
        if (response == null
                || response.response() == null
                || response.response().body() == null
                || response.response().body().items() == null
                || response.response().body().items().item() == null
                || response.response().body().items().item().isEmpty()) {
            return List.of();
        }

        // 응답에서 실제 지수 목록만 꺼냄
        List<StockMarketIndexResponseDto.IndexItem> items =
                response.response().body().items().item();

        // API 응답의 각 항목을 "분류::이름" 형태의 key Set으로 만듦 (DB 조회용)
        Set<String> targetKeys = items.stream()
                .map(item -> item.indexClassification() + "::" + item.indexName())
                .collect(Collectors.toSet());

        // DB에서 전체 지수 조회 후, API 응답에 해당하는 것만 필터링해서 Map으로 구성
        // key = "분류::이름", value = IndexInfo 엔티티
        // (a, b) -> a : 혹시 중복 key 있으면 먼저 나온 것 유지
        Map<String, IndexInfo> existingIndexMap = indexInfoRepository.findAll().stream()
                .filter(info -> targetKeys.contains(info.getIndexClassification() + "::" + info.getIndexName()))
                .collect(Collectors.toMap(
                        info -> info.getIndexClassification() + "::" + info.getIndexName(),
                        info -> info,
                        (a, b) -> a
                ));

        // 최종 저장할 지수들을 담는 Map (중복 key면 마지막 상태로 덮어씀)
        Map<String, IndexInfo> toSaveMap = new LinkedHashMap<>();
        // 신규 생성된 지수만 따로 모음 (AutoSyncConfig 생성 대상)
        List<IndexInfo> newIndexInfos = new ArrayList<>();
        // 연동 이력 저장용 리스트
        List<SyncJob> syncJobs = new ArrayList<>();

        for (StockMarketIndexResponseDto.IndexItem item : items) {
            // 현재 처리 중인 item의 key 생성
            String key = item.indexClassification() + "::" + item.indexName();

            try {
                // API 응답의 기준일자를 LocalDate로 변환
                LocalDate targetDate = LocalDate.parse(item.baseDate(), DateTimeFormatter.BASIC_ISO_DATE);
                // API 응답의 기준 시점을 LocalDate로 변환
                LocalDate basePointInTime = LocalDate.parse(item.basePointTime(), DateTimeFormatter.BASIC_ISO_DATE);

                // Map에서 기존 지수 조회 (DB 호출 없음)
                IndexInfo indexInfo = existingIndexMap.get(key);

                if (indexInfo != null) {
                    // 기존 지수가 있으면 → 정보 업데이트
                    // 즐겨찾기(favorite)는 기존 값 유지
                    IndexInfoUpdateRequestDto request = new IndexInfoUpdateRequestDto(
                            item.employedItemsCount(),
                            basePointInTime,
                            BigDecimal.valueOf(item.baseIndex()),
                            indexInfo.isFavorite()
                    );
                    indexInfo.update(request);
                    // sourceType을 OPEN_API로 변경
                    indexInfo.updateSourceType(SourceType.OPEN_API);
                } else {
                    // 기존 지수가 없으면 → 신규 생성
                    indexInfo = IndexInfo.builder()
                            .indexClassification(item.indexClassification())
                            .indexName(item.indexName())
                            .employedItemsCount(item.employedItemsCount())
                            .basePointInTime(basePointInTime)
                            .baseIndex(BigDecimal.valueOf(item.baseIndex()))
                            .sourceType(SourceType.OPEN_API)
                            .favorite(false)
                            .build();

                    // 같은 배치 내에서 동일 key가 또 나와도 중복 생성 방지
                    existingIndexMap.put(key, indexInfo);
                    // AutoSyncConfig 생성 대상에 추가
                    newIndexInfos.add(indexInfo);
                }

                // 동일 key는 마지막 상태로 1회만 저장되도록 Map에 put
                toSaveMap.put(key, indexInfo);

                // 성공 연동 이력 생성
                syncJobs.add(SyncJob.builder()
                        .jobType(JobType.INDEX_INFO)
                        .indexInfo(indexInfo)
                        .targetDate(targetDate)
                        .worker(worker)
                        .jobTime(LocalDateTime.now())
                        .result(JobResult.SUCCESS)
                        .build());

            } catch (Exception e) {
                // 처리 중 예외 발생 시 → 실패 연동 이력 생성 후 다음 item 계속 진행
                syncJobs.add(SyncJob.builder()
                        .jobType(JobType.INDEX_INFO)
                        .indexInfo(null)
                        .targetDate(null)
                        .worker(worker)
                        .jobTime(LocalDateTime.now())
                        .result(JobResult.FAILED)
                        .build());
            }
        }

        // 지수 정보 일괄 저장 (UPDATE + INSERT 한 번에)
        indexInfoRepository.saveAll(toSaveMap.values());

        // 신규 지수에 대해서만 자동 연동 설정 생성
        if (!newIndexInfos.isEmpty()) {
            List<AutoSyncConfig> autoSyncConfigsToSave = newIndexInfos.stream()
                    .map(info -> AutoSyncConfig.builder()
                            .indexInfo(info)
                            .enabled(false)
                            .build())
                    .toList();
            autoSyncConfigRepository.saveAll(autoSyncConfigsToSave);
        }

        // 저장된 연동 이력을 DTO로 변환하여 반환
        return syncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();
    }

    /*
        지수 데이터 API 연동
     */
    @Transactional
    public List<SyncJobDto> syncIndexData(String worker, List<Long> indexInfoIds, LocalDate baseDateFrom, LocalDate baseDateTo) {
        // 오늘 날짜를 API 요청용 형식으로 변환
        LocalDate today = LocalDate.now();
        String baseDate = today.format(DateTimeFormatter.BASIC_ISO_DATE);

        // baseDateFrom, baseDateTo 검증
        if (baseDateFrom.isAfter(baseDateTo)) {
            LocalDate temp = baseDateFrom;
            baseDateFrom = baseDateTo;
            baseDateTo = temp;
        }

        // 조회한 인덱스 이름을 저장할 리스트
        List<String> indexNames = new ArrayList<>();

        // indexInfoIds로 지수 이름 조회
        for (Long indexInfoId : indexInfoIds) {
            IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                            .orElseThrow(() -> new NoSuchElementException());
            indexNames.add(indexInfo.getIndexName());
        }

        // 생성된 연동 이력을 저장할 리스트
        List<SyncJob> syncJobs = new ArrayList<>();

        for (String indexName : indexNames) {

            Mono<StockMarketIndexResponseDto> apiResponses = findexOpenApiClient.fetchStockIndexData(
                    baseDateFrom.format(DateTimeFormatter.BASIC_ISO_DATE),
                    baseDateTo.format(DateTimeFormatter.BASIC_ISO_DATE),
                    indexName);
            StockMarketIndexResponseDto response = apiResponses.block();

            // response null 체크
            if (response == null || response.response() == null) {
                return List.of();
            }

            // body 추출
            StockMarketIndexResponseDto.Body body = response.response().body();
            if (body == null) {
                return List.of();
            }

            // items wrapper 추출
            StockMarketIndexResponseDto.Items itemsWrapper = body.items();
            if (itemsWrapper == null) {
                return List.of();
            }

            // 외부 API 응답 item 추출
            List<StockMarketIndexResponseDto.IndexItem> items = response.response().body().items().item();
            if (items == null || items.isEmpty()) {
                return List.of();
            }


            // 지수 별 개별 처리
            for (StockMarketIndexResponseDto.IndexItem item : items) {
                LocalDate targetDate = LocalDate.parse(item.baseDate(), DateTimeFormatter.BASIC_ISO_DATE);

                IndexInfo savedIndexInfo = null;

                try {
                    // 먼저 연결될 IndexInfo 조회
                    savedIndexInfo = indexInfoRepository.findByIndexClassificationAndIndexName(
                            item.indexClassification(),
                            item.indexName()
                    );

                    if (savedIndexInfo == null) {
                        throw new IllegalArgumentException("연결할 지수 정보가 없습니다.");
                    }

                    // 지수 정보 ID의 지수 데이터가 존재하는지 검사
                    boolean exists = indexDataRepository.existsByIndexInfoIdAndBaseDate(
                            savedIndexInfo.getId(),
                            targetDate
                    );

                    if (exists) {
                        // 기존 지수 데이터 조회
                        List<IndexData> savedIndexDatas = indexDataRepository.findByIndexInfoIdAndBaseDateBetween(
                                savedIndexInfo.getId(),
                                baseDateFrom,
                                baseDateTo
                        );

                        for (IndexData indexData : savedIndexDatas) {
                            // sourceType은 변경하지 않고 지수 정보 수정
                            IndexDataUpdateRequestDto request = new IndexDataUpdateRequestDto(
                                    BigDecimal.valueOf(item.marketOpeningPrice()),
                                    BigDecimal.valueOf(item.closingPrice()),
                                    BigDecimal.valueOf(item.highPrice()),
                                    BigDecimal.valueOf(item.lowPrice()),
                                    BigDecimal.valueOf(item.versus()),
                                    BigDecimal.valueOf(item.fluctuationRate()),
                                    BigInteger.valueOf(item.tradingVolume()),
                                    BigInteger.valueOf(item.tradingPrice()),
                                    BigInteger.valueOf(item.listingMarketTotalAmount())
                            );

                            indexData.update(request);
                            indexDataRepository.save(indexData);

                        }
                    } else {
                        // 기존 지수 데이터가 존재하지 않으면 신규 생성
                        IndexData createdIndexData = IndexData.builder()
                                .indexInfoId(savedIndexInfo.getId())
                                .baseDate(targetDate)
                                .sourceType(com.sprint.mission.findex.indexdata.entity.SourceType.OPEN_API)
                                .marketPrice(BigDecimal.valueOf(item.marketOpeningPrice()))
                                .closingPrice(BigDecimal.valueOf(item.highPrice()))
                                .highPrice(BigDecimal.valueOf(item.highPrice()))
                                .lowPrice(BigDecimal.valueOf(item.lowPrice()))
                                .versus(BigDecimal.valueOf(item.versus()))
                                .fluctuationRate(BigDecimal.valueOf(item.fluctuationRate()))
                                .tradingQuantity(BigInteger.valueOf(item.tradingVolume()))
                                .tradingPrice(BigInteger.valueOf(item.tradingPrice()))
                                .marketTotalAmount(BigInteger.valueOf(item.listingMarketTotalAmount()))
                                .build();

                        indexDataRepository.save(createdIndexData);
                    }

                    // 해당 지수 연동 성공 이력 저장
                    SyncJob successSyncJob = SyncJob.builder()
                            .jobType(JobType.INDEX_DATA)
                            .indexInfo(savedIndexInfo)
                            .targetDate(targetDate)
                            .worker(worker)
                            .jobTime(LocalDateTime.now())
                            .result(JobResult.SUCCESS)
                            .build();

                    syncJobRepository.save(successSyncJob);
                    syncJobs.add(successSyncJob);

                } catch (Exception e) {
                    // 특정 지수 처리 실패 시 실패 이력 저장 후 다음 지수 계속 진행
                    SyncJob failureSyncJob = SyncJob.builder()
                            .jobType(JobType.INDEX_DATA)
                            .indexInfo(savedIndexInfo)
                            .targetDate(targetDate)
                            .worker(worker)
                            .jobTime(LocalDateTime.now())
                            .result(JobResult.FAILED)
                            .build();

                    syncJobRepository.save(failureSyncJob);
                    syncJobs.add(failureSyncJob);
                }
            }
        }
        // 저장된 연동 이력을 DTO로 변환하여 반환
        // 연동 이력 일괄 저장
        syncJobRepository.saveAll(syncJobs);

        // 저장된 연동 이력을 DTO로 변환해서 반환
        return syncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();
    }
}
