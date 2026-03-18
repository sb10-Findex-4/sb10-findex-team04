package com.sprint.mission.findex.autosyncconfig.scheduler;

import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import com.sprint.mission.findex.autosyncconfig.service.AutoSyncConfigService;
import com.sprint.mission.findex.autosyncconfig.service.AutoSyncExecutor;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 자동 연동 스케줄러
 * enabled=true인 대상 조회
 * 연동 대상의 IndexInfo 추출 -> 각 IndexInfo 별 자동 연동 수행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AutoSyncScheduler {

  private final AutoSyncConfigService autoSyncConfigService;
  private final AutoSyncExecutor autoSyncExecutor;

  /**
   * 명세서에 실행 시간은 정의되어 있지 않음
   * 하루 1회 실행으로 설정 이라고 명시되어 있으므로
   * 매일 0시에 자동 연동 대상을 조회한다.
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void runAutoSync() {

    log.info("자동 연동 스케줄러 시작");

    List<AutoSyncConfig> autoSyncConfigs =
        autoSyncConfigService.findAllEnabledAutoSyncConfigs();

    if (autoSyncConfigs.isEmpty()) {
      log.info("자동 연동 대상 없음");
      return;
    }

    for (AutoSyncConfig autoSyncConfig : autoSyncConfigs) {
      IndexInfo indexInfo = autoSyncConfig.getIndexInfo();

      log.info("자동 연동 대상 처리 시작 indexInfoId={}, indexName={}",
          indexInfo.getId(),
          indexInfo.getIndexName());

      try {
        autoSyncExecutor.execute(indexInfo);
      } catch (Exception e) {
        log.error("자동 연동 실패 indexInfoId={}", indexInfo.getId(), e);
      }
    }

    log.info("자동 연동 스케줄러 종료");
  }
}
