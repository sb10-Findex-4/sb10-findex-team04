package com.sprint.mission.findex.autosyncconfig.dto.request;

public record AutoSyncConfigSearchRequestDto(
    Long indexInfoId,
    Boolean enabled,
    String cursor,
    String sortField,
    String sortDirection,
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