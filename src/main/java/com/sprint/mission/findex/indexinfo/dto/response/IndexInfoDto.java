
package com.sprint.mission.findex.indexinfo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
/*
지수 정보 응답 Dto
 */
public record IndexInfoDto(
        Long id,
        String indexClassification,
        String indexName,
        Integer employedItemsCount,
        LocalDate basePointInTime,
        BigDecimal baseIndex,
        String sourceType,
        Boolean favorite
) {
}