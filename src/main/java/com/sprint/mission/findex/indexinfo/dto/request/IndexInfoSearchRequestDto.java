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
        Long idAfter,

        String cursor, // 다음 페이지 시작점
        String sortField, // 정렬 필드
        String sortDirection, // 정렬 방향
        Integer size // 페이지 크기
) {
    public IndexInfoSearchRequestDto {
        if (size == null) {
            size = 10;
        }

        if (sortField == null) {
            sortField = "indexClassification";
        }

        if (sortDirection == null) {
            sortDirection = "asc";
        }
    }
}
