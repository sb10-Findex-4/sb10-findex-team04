package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.client.FindexOpenApiClient;
import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.service.IndexDataService;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncJobRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class  AutoSyncExecutorImpl implements AutoSyncExecutor {

  private final FindexOpenApiClient openApiClient;
  private final IndexDataService indexDataService;
  private final SyncJobRepository syncJobRepository;

  @Override
  public void execute(IndexInfo indexInfo) {

    log.info("자동 연동 시작 indexInfoId={}, indexName={}",
        indexInfo.getId(),
        indexInfo.getIndexName());

    try {

      // 마지막 성공 날짜 조회
      Optional<SyncJob> lastSuccessJob =
          syncJobRepository.findTopByResultOrderByJobTimeDesc(JobResult.SUCCESS);

      LocalDate startDate = lastSuccessJob
          .map(SyncJob::getTargetDate)
          .orElse(LocalDate.now().minusDays(1));

      LocalDate today = LocalDate.now();

      log.info("자동 연동 범위 startDate={}, today={}", startDate, today);

      for (LocalDate date = startDate.plusDays(1);
          !date.isAfter(today);
          date = date.plusDays(1)) {

        try {

          String baseDate = date.toString();

          // API 호출
          StockMarketIndexResponseDto response =
              openApiClient.fetchStockIndexData(baseDate, indexInfo.getIndexName())
                  .block();

          // response null 체크
          if (response == null || response.response() == null) {
            log.warn("API 응답 없음 date={}", baseDate);
            continue;
          }

          // body 추출
          StockMarketIndexResponseDto.Body body = response.response().body();
          if (body == null) {
            log.warn("body 없음 date={}", baseDate);
            continue;
          }

          // items wrapper 추출
          StockMarketIndexResponseDto.Items itemsWrapper = body.items();
          if (itemsWrapper == null) {
            log.warn("items 없음 date={}", baseDate);
            continue;
          }

          // 실제 데이터 리스트 추출
          List<StockMarketIndexResponseDto.IndexItem> items = itemsWrapper.item();
          if (items == null || items.isEmpty()) {
            log.warn("데이터 없음 date={}", baseDate);
            continue;
          }

          for (StockMarketIndexResponseDto.IndexItem item : items) {
            try {

              IndexDataCreateRequestDto request = new IndexDataCreateRequestDto(
                  indexInfo.getId(),
                  LocalDate.parse(item.baseDate()),

                  toDecimal(item.marketOpeningPrice()),
                  toDecimal(item.closingPrice()),
                  toDecimal(item.highPrice()),
                  toDecimal(item.lowPrice()),
                  toDecimal(item.versus()),
                  toDecimal(item.fluctuationRate()),

                  toInteger(item.tradingVolume()),
                  toInteger(item.tradingPrice()),
                  toInteger(item.listingMarketTotalAmount())
              );

              indexDataService.create(request);

            } catch (Exception e) {
              log.warn("데이터 저장 실패 (무시)", e);
            }
          }

          // 성공 기록
          syncJobRepository.save(SyncJob.builder()
              .jobType(JobType.INDEX_DATA)
              .targetDate(date)
              .worker("AUTO-SCHEDULER")
              .jobTime(LocalDateTime.now())
              .result(JobResult.SUCCESS)
              .build());

        } catch (Exception e) {

          // 실패 기록
          syncJobRepository.save(SyncJob.builder()
              .jobType(JobType.INDEX_DATA)
              .targetDate(date)
              .worker("AUTO-SCHEDULER")
              .jobTime(LocalDateTime.now())
              .result(JobResult.FAILED)
              .build());

          log.error("자동 연동 실패 date={}", date, e);
        }
      }

    } catch (Exception e) {
      log.error("자동 연동 전체 실패 indexInfoId={}", indexInfo.getId(), e);
    }

    log.info("자동 연동 종료 indexInfoId={}", indexInfo.getId());
  }

  private BigDecimal toDecimal(Double value) {
    if (value == null) {
      return BigDecimal.ZERO;
    }
    return BigDecimal.valueOf(value);
  }

  private BigInteger toInteger(Long value) {
    if (value == null) {
      return BigInteger.ZERO;
    }
    return BigInteger.valueOf(value);
  }
}
