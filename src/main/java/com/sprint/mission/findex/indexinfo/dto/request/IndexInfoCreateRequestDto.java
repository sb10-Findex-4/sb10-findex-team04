package com.sprint.mission.findex.indexinfo.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 생성 Dto (사용자 수동)
 */
public record IndexInfoCreateRequestDto(
        @NotBlank
        String indexClassification,

        @NotBlank
        String indexName,

        @NotNull
        @PositiveOrZero
        Integer employedItemsCount,

        @NotNull
        @PastOrPresent
        LocalDate basePointInTime,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal baseIndex,

        Boolean favorite
) {
}
