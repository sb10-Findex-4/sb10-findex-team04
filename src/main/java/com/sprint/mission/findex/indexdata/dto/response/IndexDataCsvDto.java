package com.sprint.mission.findex.indexdata.dto.response;

import com.opencsv.bean.CsvBindByName;
import com.sprint.mission.findex.indexdata.entity.SourceType;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Schema(description = "CSV export 지수 데이터 DTO")
public record IndexDataCsvDto(
    @Schema(description = "데이터 고유 ID", example = "100")
    @CsvBindByName(column = "id")
    Long id,

    @Schema(description = "지수 정보 ID", example = "1")
    @CsvBindByName(column = "index_info_id")
    Long indexInfoId,

    @Schema(description = "기준 일자", example = "2026-03-19")
    @CsvBindByName(column = "base_date")
    LocalDate baseDate,

    @Schema(description = "데이터 출처", example = "API")
    @CsvBindByName(column = "source_type")
    SourceType sourceType,

    @Schema(description = "시가", example = "2800.25")
    @CsvBindByName(column = "market_price")
    BigDecimal marketPrice,

    @Schema(description = "종가", example = "2850.75")
    @CsvBindByName(column = "closing_price")
    BigDecimal closingPrice,

    @Schema(description = "고가", example = "2870.5")
    @CsvBindByName(column = "high_price")
    BigDecimal highPrice,

    @Schema(description = "저가", example = "2795.3")
    @CsvBindByName(column = "low_price")
    BigDecimal lowPrice,

    @Schema(description = "전일 대비 등락폭", example = "50.5")
    @CsvBindByName(column = "versus")
    BigDecimal versus,

    @Schema(description = "전일 대비 등락률", example = "1,8")
    @CsvBindByName(column = "fluctuation_rate")
    BigDecimal fluctuationRate,

    @Schema(description = "거래량", example = "1250000")
    @CsvBindByName(column = "trading_quantity")
    BigInteger tradingQuantity,

    @Schema(description = "거래대금", example = "3500000000")
    @CsvBindByName(column = "trading_price")
    BigInteger tradingPrice,

    @Schema(description = "상장 시가 총액", example = "450000000000")
    @CsvBindByName(column = "market_total_amount")
    BigInteger marketTotalAmount
) {
}
