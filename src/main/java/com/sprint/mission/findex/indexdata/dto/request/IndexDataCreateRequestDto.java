package com.sprint.mission.findex.indexdata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Schema(description = "지수 시세 데이터 생성 요청 DTO")
public record IndexDataCreateRequestDto(
    @Schema(description = "지수 정보 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long indexInfoId,               // 지수 정보 ID

    @Schema(description = "기준 일자", example = "2026-03-19", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    LocalDate baseDate,             // 기준 일자

    @Schema(description = "시가", example = "2800.25", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal marketPrice,         // 시가: 정규시장의 매매시간 개시 후 형성되는 최초 가격

    @Schema(description = "종가", example = "2850.75", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal closingPrice,        // 종가: 정규시장의 매매시간 종료시까지 형성되는 최종 가격

    @Schema(description = "고가", example = "2870.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal highPrice,           // 고가: 하루 중 지수의 최고치

    @Schema(description = "저가", example = "2795.3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal lowPrice,            // 저가: 하루 중 지수의 최저치

    @Schema(description = "전일 대비 등락폭", example = "50.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal versus,              // 대비: 전일 대비 등락

    @Schema(description = "전일 대비 등락률", example = "1.8", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigDecimal fluctuationRate,     // 등락률: 전일 대비 등락에 따른 비율

    @Schema(description = "거래량", example = "1250000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigInteger tradingQuantity,     // 거래량: 지수에 포함된 종목의 거래량 총합

    @Schema(description = "거래대금", example = "3500000000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigInteger tradingPrice,        // 거래대금: 지수에 포함된 종목의 거래대금 총합

    @Schema(description = "상장 시가 총액", example = "450000000000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    BigInteger marketTotalAmount    // 상장 시가 총액: 지수에 포함된 종목의 시가 총액
) {
}
