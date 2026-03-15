package com.sprint.mission.findex.indexinfo.dto.response;

/*
지수 정보 요약 Dto
 */
public record IndexInfoSummaryDto(
        Long id,                    // 지수 정보 id
        String indexClassification, // 지수 분류명
        String indexName            // 지명
) {
}
