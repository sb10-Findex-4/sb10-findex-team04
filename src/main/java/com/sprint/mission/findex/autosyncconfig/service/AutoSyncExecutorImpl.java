package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class AutoSyncExecutorImpl implements AutoSyncExecutor {

  @Override
  public void execute(IndexInfo indexInfo) {

    log.info("자동 연동 실행 시작 indexInfoId={}, indexName={}",
        indexInfo.getId(),
        indexInfo.getIndexName());

    // TODO: 다음 단계에서 실제 자동 연동 로직 구현

    log.info("자동 연동 실행 종료 indexInfoId={}", indexInfo.getId());
  }
}