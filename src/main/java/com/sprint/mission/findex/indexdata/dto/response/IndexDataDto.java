package com.sprint.mission.findex.indexdata.dto.response;

import com.sprint.mission.findex.indexdata.entity.SourceType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public record IndexDataDto(
        Long id,
        Long indexInfoId,           // 지수 정보 ID
        LocalDate baseDate,         // 기준 날짜
        SourceType sourceType,      // 소스 타입, 입력 출처
        BigDecimal marketPrice,     // 시가
        BigDecimal closingPrice,    // 종가
        BigDecimal highPrice,       // 고가
        BigDecimal lowPrice,        // 저가
        BigDecimal versus,          // 대비
        BigDecimal fluctuationRate, // 등락률
        BigInteger tradingQuantity, // 거래량
        BigInteger tradingPrice,    // 거래 대금
        BigInteger marketTotalAmount// 상장 시가 총액
) {
}
