package com.sprint.mission.findex.indexinfo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 응답 Dto
 */
@Schema(description = "지수 정보 DTO")
public record IndexInfoDto(
        @Schema(description = "지수 정보 ID", example = "1")
        Long id,

        @Schema(description = "지수 분류명", example = "KOSPI시리즈")
        String indexClassification,

        @Schema(description = "지수명", example = "IT 서비스")
        String indexName,

        @Schema(description = "채용 종목 수", example = "200")
        Integer employedItemsCount,

        @Schema(description = "기준 시점", example = "2000-01-01")
        LocalDate basePointInTime,

        @Schema(description = "기준 지수", example = "1000.00")
        BigDecimal baseIndex,

        @Schema(description = "출처 (사용자, Open API)", allowableValues = {"USER", "OPEN_API"}, example = "OPEN_API")
        String sourceType,

        @Schema(description = "즐겨찾기 여부", example = "true")
        Boolean favorite
) {
}
