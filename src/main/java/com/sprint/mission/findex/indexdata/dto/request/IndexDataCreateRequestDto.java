package com.sprint.mission.findex.indexdata.dto.request;

import java.math.BigDecimal;import java.math.BigInteger;
import java.time.LocalDate;

public record IndexDataCreateRequestDto(
        Long indexInfoId,
        LocalDate baseDate,
        BigDecimal marketPrice,
        BigDecimal closingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigInteger tradingQuantity,
        BigInteger tradingPrice,
        BigInteger marketToTalAmount
) {
}
