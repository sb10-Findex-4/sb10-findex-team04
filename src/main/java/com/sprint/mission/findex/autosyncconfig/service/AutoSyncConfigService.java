package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import java.util.List;

public interface AutoSyncConfigService {
  // 전체 목록 조회
  CursorPageResponseAutoSyncConfigDto findAll(
      Long indexInfoId,
      Boolean enabled,
      String cursor,
      String sortField,
      String sortDirection,
      int size
  );

  AutoSyncConfigDto update(Long id, AutoSyncConfigUpdateRequestDto request);

  List<AutoSyncConfig> findAllEnabledAutoSyncConfigs();
}
