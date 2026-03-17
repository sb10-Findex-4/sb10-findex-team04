package com.sprint.mission.findex.indexdata.dto.request;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

public record IndexDataUpdateRequestDto(
        @Nullable
        BigDecimal marketPrice,         // 시가: 정규시장의 매매시간 개시 후 형성되는 최초 가격
        @Nullable
        BigDecimal closingPrice,        // 종가: 정규시장의 매매시간 종료시까지 형성되는 최종 가격
        @Nullable
        BigDecimal highPrice,           // 고가: 하루 중 지수의 최고치
        @Nullable
        BigDecimal lowPrice,            // 저가: 하루 중 지수의 최저치
        @Nullable
        BigDecimal versus,              // 대비: 전일 대비 등락
        @Nullable
        BigDecimal fluctuationRate,     // 등락률: 전일 대비 등락에 따른 비율
        @Nullable
        BigInteger tradingQuantity,     // 거래량: 지수에 포함된 종목의 거래량 총합
        @Nullable
        BigInteger tradingPrice,        // 거래대금: 지수에 포함된 종목의 거래대금 총합
        @Nullable
        BigInteger marketTotalAmount    // 상장 시가 총액: 지수에 포함된 종목의 시가 총액
) {
}
