package com.sprint.mission.findex.indexdata.dto.data;

import com.opencsv.bean.CsvBindByName;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.entity.SourceType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public record IndexDataCsvDto(
        @CsvBindByName(column = "id")
        Long id,

        @CsvBindByName(column = "index_info_id")
        Long indexInfoId,

        @CsvBindByName(column = "base_date")
        LocalDate baseDate,

        @CsvBindByName(column = "source_type")
        SourceType sourceType,

        @CsvBindByName(column = "market_price")
        BigDecimal marketPrice,

        @CsvBindByName(column = "closing_price")
        BigDecimal closingPrice,

        @CsvBindByName(column = "high_price")
        BigDecimal highPrice,

        @CsvBindByName(column = "low_price")
        BigDecimal lowPrice,

        @CsvBindByName(column = "versus")
        BigDecimal versus,

        @CsvBindByName(column = "fluctuation_rate")
        BigDecimal fluctuationRate,

        @CsvBindByName(column = "trading_quantity")
        BigInteger tradingQuantity,

        @CsvBindByName(column = "trading_price")
        BigInteger tradingPrice,

        @CsvBindByName(column = "market_total_amount")
        BigInteger marketTotalAmount
) {
}
