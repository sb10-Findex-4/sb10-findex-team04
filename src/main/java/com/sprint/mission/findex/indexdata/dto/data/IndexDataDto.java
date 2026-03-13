package com.sprint.mission.findex.indexdata.dto.data;

import com.sprint.mission.findex.indexdata.entity.SourceType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public record IndexDataDto(
        Long id,
        Long indexInfoId,
        LocalDate basedate,
        SourceType sourceType,
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
