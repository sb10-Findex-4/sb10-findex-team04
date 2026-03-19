package com.sprint.mission.findex.autosyncconfig.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "자동 연동 설정 조회 요청")
public record AutoSyncConfigSearchRequestDto(
        @Schema(description = "지수 정보 ID", example = "1")
        Long indexInfoId,

        @Schema(description = "활성화 여부", example = "false")
        Boolean enabled,

        @Schema(description = "커서", example = "eyJpZCI6MjB9")
        String cursor,

        @Schema(description = "정렬 기준", example = "sortDirection")
        String sortField,

        @Schema(description = "정렬 방향", example = "DESC")
        String sortDirection,

        @Schema(description = "페이지 크기", example = "10")
        Integer size
) {
  public String sortField() {
    return sortField == null ? "id" : sortField;
  }

  public String sortDirection() {
    return sortDirection == null ? "DESC" : sortDirection;
  }

  public Integer size() {
    return size == null ? 10 : size;
  }
}