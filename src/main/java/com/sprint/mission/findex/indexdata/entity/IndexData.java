package com.sprint.mission.findex.indexdata.entity;

import com.sprint.mission.findex.base.BaseEntity;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;import jakarta.persistence.Column;
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
    // 지수 정보 ID
    @Column(name = "index_info_id", nullable = false)
    private Long indexInfoId;

    // 기준 날짜
    @Column(name = "base_date", nullable = false)
    private Date baseDate;

    // 소스 타입 USER/OPEN_API
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    // 시가: 정규시장의 매매시간 개시 후 형성되는 최초 가격
    @Column(name = "market_price", nullable = false)
    private BigDecimal marketPrice;

    // 종가: 정규시장의 매매시간 종료시까지 형성되는 최종 가격
    @Column(name = "closing_price", nullable = false)
    private BigDecimal closingPrice;

    // 고가: 하루 중 지수의 최고치
    @Column(name = "high_price", nullable = false)
    private BigDecimal highPrice;

    // 저가: 하루 중 지수의 최저치
    @Column(name = "low_price", nullable = false)
    private BigDecimal lowPrice;

    // 대비: 전일 대비 등락
    @Column(name = "versus", nullable = false)
    private BigDecimal versus;

    // 등락률: 전일 대비 등락에 따른 비율
    @Column(name = "fluctuation_rate", nullable = false)
    private BigDecimal fluctuationRate;

    // 거래량: 지수에 포함된 종목의 거래량 총합
    @Column(name = "trading_quantiy", nullable = false)
    private BigInteger tradingQuantity;

    // 거래대금: 지수에 포함된 종목의 거래대금 총합
    @Column(name = "trading_price", nullable = false)
    private BigInteger tradingPrice;

    // 상장 시가 총액: 지수에 포함된 종목의 시가 총액
    @Column(name = "market_total_amount", nullable = false)
    private BigInteger marketTotalAmount;

    public void update(IndexDataUpdateRequestDto request) {
        this.marketPrice = request.marketPrice();
        this.closingPrice = request.closingPrice();
        this.highPrice = request.highPrice();
        this.lowPrice = request.lowPrice();
        this.versus = request.versus();
        this.fluctuationRate = request.fluctuationRate();
        this.tradingPrice = request.tradingPrice();
        this.marketTotalAmount = request.marketTotalAmount();
    }
}
