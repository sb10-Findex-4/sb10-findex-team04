package com.sprint.mission.findex.indexinfo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoDto(
        Long id,
        String indexClassification,
        String indexName,
        Integer employedItemsCount,
        LocalDate basePointInTime,
        BigDecimal baseIndex,
        Boolean favorite
) {
}
