package com.sprint.mission.findex.indexinfo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 수정 Dto
 */
@Schema(description = "지수 정보 수정 요청")
public record IndexInfoUpdateRequestDto(
        @Schema(description = "채용 종목 수", example = "200")
        @PositiveOrZero
        Integer employedItemsCount,

        @Schema(description = "기준 시점", example = "2000-01-01")
        @PastOrPresent
        LocalDate basePointInTime,

        @Schema(description = "기준 지수", example = "1000.00")
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        @Schema(description = "즐겨찾기 여부", example = "true")
        Boolean favorite
) {
}
