package com.sprint.mission.findex.indexinfo.dto.request;
/*
지수 정보 목록 조회 Dto
 */
public record IndexInfoSearchRequestDto(
        String indexClassification,
        String indexName,
        boolean favorite,
        Long isAfter,
        String cursor,
        String sortField,
        String sortDirection,
        int size
) {
}
