package com.sprint.mission.findex.indexdata.dto.request;

import com.sprint.mission.findex.indexdata.entity.SourceType;

import java.math.BigDecimal;import java.math.BigInteger;
import java.time.LocalDate;

public record IndexDataCreateRequestDto(
        Long indexInfoId,               // 지수 정보 ID
        LocalDate baseDate,             // 기준 일자
        BigDecimal marketPrice,         // 시가: 정규시장의 매매시간 개시 후 형성되는 최초 가격
        BigDecimal closingPrice,        // 종가: 정규시장의 매매시간 종료시까지 형성되는 최종 가격
        BigDecimal highPrice,           // 고가: 하루 중 지수의 최고치
        BigDecimal lowPrice,            // 저가: 하루 중 지수의 최저치
        BigDecimal versus,              // 대비: 전일 대비 등락
        BigDecimal fluctuationRate,     // 등락률: 전일 대비 등락에 따른 비율
        BigInteger tradingQuantity,     // 거래량: 지수에 포함된 종목의 거래량 총합
        BigInteger tradingPrice,        // 거래대금: 지수에 포함된 종목의 거래대금 총합
        BigInteger marketToTalAmount    // 상장 시가 총액: 지수에 포함된 종목의 시가 총액
) {
}
