package com.sprint.mission.findex.autosyncconfig.dto.response;

// 조회 응답 DTO
public record AutoSyncConfigDto (
    Long id,
    Long indexInfoId,
    String indexClassification,
    String indexName,
    boolean enabled
) {
}
