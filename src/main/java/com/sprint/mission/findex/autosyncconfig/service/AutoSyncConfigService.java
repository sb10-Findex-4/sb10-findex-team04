package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;

public interface AutoSyncConfigService {
  // 전체 목록 조회
  CursorPageResponseAutoSyncConfigDto findAll(
      Long indexInfoId,
      Boolean enabled,
      Long cursor,
      int pageSize
  );

  AutoSyncConfigDto update(Long id, AutoSyncConfigUpdateRequestDto request);
}
