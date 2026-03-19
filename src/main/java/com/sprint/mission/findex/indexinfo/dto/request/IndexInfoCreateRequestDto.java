package com.sprint.mission.findex.indexinfo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 생성 Dto (사용자 수동)
 */
@Schema(description = "지수 정보 생성 요청 DTO")
public record IndexInfoCreateRequestDto(
        @Schema(description = "지수 분류명", example = "KOSPI시리즈", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String indexClassification,

        @Schema(description = "지수명", example = "IT 서비스", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String indexName,

        @Schema(description = "채용 종목 수", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PositiveOrZero
        Integer employedItemsCount,

        @Schema(description = "기준 시점", example = "2000-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PastOrPresent
        LocalDate basePointInTime,

        @Schema(description = "기준 지수", example = "1000.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        @Schema(description = "즐겨찾기 여부", example = "false")
        Boolean favorite
) {
}
