package com.sprint.mission.findex.autosyncconfig.dto.response;

import java.util.List;

// 커서 기반 페이지 응답 DTO
public record CursorPageResponseAutoSyncConfigDto (
  List<AutoSyncConfigDto> content,
  String nextCursor,
  Long nextIdAfter,
  int size,
  long totalElements,
  boolean hasNext
) {
}
