package com.sprint.mission.findex.indexdata.entity;

import com.sprint.mission.findex.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;import lombok.Getter;import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;import java.math.BigInteger;import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "index_data",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"index_info_id", "base_date"})
        }
)
public class IndexData extends BaseEntity {
    @Column(name = "index_info_id", nullable = false)
    private Long indexInfoId;
    @Column(name = "base_date", nullable = false)
    private Date baseDate;
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;
    @Column(name = "market_price", nullable = false)
    private BigDecimal marketPrice;
    @Column(name = "closing_price", nullable = false)
    private BigDecimal closingPrice;
    @Column(name = "high_price", nullable = false)
    private BigDecimal highPrice;
    @Column(name = "low_price", nullable = false)
    private BigDecimal lowPrice;
    @Column(name = "versus", nullable = false)
    private BigDecimal versus;
    @Column(name = "fluctuation_rate", nullable = false)
    private BigDecimal fluctuationRate;
    @Column(name = "trading_quantiy", nullable = false)
    private BigInteger tradingQuantity;
    @Column(name = "trading_price", nullable = false)
    private BigInteger tradingPrice;
    @Column(name = "market_total_amount", nullable = false)
    private BigInteger marketTotalAmount;

    /*
    public IndexData(
            Long indexInfoId,
            Date baseDate,
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
        super();
        //
        this.indexInfoId = indexInfoId;
        this.baseDate = baseDate;
        this.sourceType = sourceType;
        this.marketPrice = marketPrice;
        this.closingPrice = closingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.versus = versus;
        this.fluctuationRate = fluctuationRate;
        this.tradingQuantity = tradingQuantity;
        this.tradingPrice = tradingPrice;
        this.marketTotalAmount = marketTotalAmount;
    }
    */
}
