package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.indexinfo.entity.IndexInfo;

public interface AutoSyncExecutor {

  void execute(IndexInfo indexInfo);
}