package com.sprint.mission.findex.indexinfo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoCreateRequest(
        @NotBlank
        String indexClassification,

        @NotBlank
        String indexName,

        @NotNull
        @PositiveOrZero
        Integer employedItemsCount,

        @NotNull
        LocalDate basePointInTime,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        Boolean favorite
) {
}
