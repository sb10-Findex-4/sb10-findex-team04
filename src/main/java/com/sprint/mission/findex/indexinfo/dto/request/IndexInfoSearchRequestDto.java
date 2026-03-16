package com.sprint.mission.findex.indexinfo.dto.request;
/*
지수 정보 목록 조회 Dto
 */
public record IndexInfoSearchRequestDto(
        // 지수 분류명
        String indexClassification,
        // 지수명
        String indexName,
        // 즐겨찾기
        Boolean favorite,
        // 커서 (id)
        Long isAfter,

        String cursor,
        String sortField,
        String sortDirection,
        Integer size
) {
    public IndexInfoSearchRequestDto {
        if (size == null) {
            size = 10;
        }

        if (sortField == null) {
            sortField = indexClassification;
        }

        if (sortDirection == null) {
            sortDirection = "asc";
        }
    }
}
