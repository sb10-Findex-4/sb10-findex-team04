package com.sprint.mission.findex.indexinfo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/*
지수 정보 요약 Dto
 */
@Schema(description = "지수 정보 요약 DTO")
public record IndexInfoSummaryDto(
        @Schema(description = "지수 정보 ID", example = "1")
        Long id,                    // 지수 정보 id

        @Schema(description = "지수 분류명", example = "KOSPI시리즈")
        String indexClassification, // 지수 분류명

        @Schema(description = "지수명", example = "IT 서비스")
        String indexName            // 지명
) {
}
