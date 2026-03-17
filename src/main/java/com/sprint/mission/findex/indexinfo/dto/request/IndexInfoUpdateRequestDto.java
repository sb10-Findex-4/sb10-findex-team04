package com.sprint.mission.findex.indexinfo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 수정 Dto
 */
public record IndexInfoUpdateRequestDto(
        @PositiveOrZero
        Integer employedItemsCount,

        @PastOrPresent
        LocalDate basePointInTime,

        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        Boolean favorite
) {
}
