package com.sprint.mission.findex.indexinfo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/*
지수 정보 목록 조회 Dto
 */
@Schema(description = "지수 정보 조회 요청")
public record IndexInfoSearchRequestDto(
        // 지수 분류명
        @Schema(description = "지수 분류명", example = "KOSPI시리즈")
        String indexClassification,

        // 지수명
        @Schema(description = "지수명", example = "IT 서비스")
        String indexName,

        // 즐겨찾기
        @Schema(description = "즐겨찾기 여부", example = "true")
        Boolean favorite,

        // 커서 (id)
        @Schema(description = "이전 페이지 마지막 요소 ID", example = "eyJpZCI6MjB9")
        Long idAfter,

        @Schema(description = "커서 (다음 페이지 시작점)", example = "eyJpZCI6MjB9")
        String cursor, // 다음 페이지 시작점

        @Schema(description = "정렬 필드", defaultValue = "indexClassification", allowableValues = {"indexClassification", "indexName", "employedItemsCount"})
        String sortField, // 정렬 필드

        @Schema(description = "정렬 방향", defaultValue = "asc", allowableValues = {"asc", "desc"})
        String sortDirection, // 정렬 방향

        @Schema(description = "페이지 크기", defaultValue = "10")
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
