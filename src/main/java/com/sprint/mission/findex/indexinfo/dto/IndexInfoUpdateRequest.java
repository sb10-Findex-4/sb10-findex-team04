package com.sprint.mission.findex.indexinfo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoUpdateRequest(
        @PositiveOrZero
        Integer employedItemsCount,

        LocalDate basePointInTime,

        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        Boolean favorite
) {
}
