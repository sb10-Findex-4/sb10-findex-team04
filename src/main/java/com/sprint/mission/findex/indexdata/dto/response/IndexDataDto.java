package com.sprint.mission.findex.indexdata.dto.response;

import com.sprint.mission.findex.indexdata.entity.SourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Schema(description = "지수 데이터 DTO")
public record IndexDataDto(
    @Schema(description = "데이터 고유 ID", example = "100")
    Long id,

    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,           // 지수 정보 ID

    @Schema(description = "기준 일자", example = "2026-03-19")
    LocalDate baseDate,         // 기준 날짜

    @Schema(description = "데이터 출처", example = "API")
    SourceType sourceType,      // 소스 타입, 입력 출처

    @Schema(description = "시가", example = "2800.25")
    BigDecimal marketPrice,     // 시가

    @Schema(description = "종가", example = "2850.75")
    BigDecimal closingPrice,    // 종가

    @Schema(description = "고가", example = "2870.5")
    BigDecimal highPrice,       // 고가

    @Schema(description = "저가", example = "2795.3")
    BigDecimal lowPrice,        // 저가

    @Schema(description = "전일 대비 등락폭", example = "50.5")
    BigDecimal versus,          // 대비

    @Schema(description = "전일 대비 등락률", example = "1,8")
    BigDecimal fluctuationRate, // 등락률

    @Schema(description = "거래량", example = "1250000")
    BigInteger tradingQuantity, // 거래량

    @Schema(description = "거래대금", example = "3500000000")
    BigInteger tradingPrice,    // 거래 대금

    @Schema(description = "상장 시가 총액", example = "450000000000")
    BigInteger marketTotalAmount// 상장 시가 총액
) {
}
