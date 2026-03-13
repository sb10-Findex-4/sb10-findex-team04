package com.sprint.mission.findex.indexdata.dto.request;

import java.math.BigDecimal;
import java.math.BigInteger;

public record IndexDataUpdateRequestDto(
        BigDecimal marketPrice,
        BigDecimal closingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigInteger tradingQuantity,
        BigInteger tradingPrice,
        BigInteger marketTotalAmount
) {
}
