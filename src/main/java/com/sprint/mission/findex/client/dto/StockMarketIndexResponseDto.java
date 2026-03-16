package com.sprint.mission.findex.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*
    공공데이터포털 (금융위원회) API의 응답 구조
    -------------------------
    공공데이터포털 API 응답 규격의 약자(basDt 등)을 도메인 용어에 맞게 재정의
    @JsonProperty를 사용하여 JSON 필드와 IndexItem 필드를 매핑
    외부 API 의존성을 격리하기 위해 ResponseDto 구조를 캡슐화
 */
public record StockMarketIndexResponseDto(Response response) {
    // 공공데이터포털 API의 최상위 응답 객체: 헤더와 바디로 구성
    public record Response(Header header, Body body) {}

    // 응답 헤더: 결과 정보(Header)와 실제 데이터(Body)
    public record Header(String resultCode, String resultMsg) {}

    // 응답 바디: 페이징 정보와 실제 지수 목록
    public record Body(int numOfRows,           // 한 페이지에 존재하는 결과 개수
                       int pageNo,              // 페이지 번호
                       int totalCount,          // 전체 결과 수
                       Items items              // 조회된 지수 목록 객체
    ) {}

    // 지수 목록 객체
    public record Items(List<IndexItem> item) {}

    /*
        지수 객체 구조
     */
    public record IndexItem(
            @JsonProperty("lsYrEdVsFltRt")
            Double lastYearEndVsFluctuationRate,           // 지수의 전년말대비 등락율

            @JsonProperty("basPntm")
            String basePointTime,                          // 지수를 산출하기 위한 기준 시점

            @JsonProperty("basIdx")
            Double baseIndex,                              // 기준 시점의 지수값

            @JsonProperty("basDt")
            String baseDate,                               // 기준 일자

            @JsonProperty("idxCsf")
            String indexClassification,                    // 지수 분류 명칭

            @JsonProperty("idxNm")
            String indexName,                              // 지수명

            @JsonProperty("epyItmsCnt")
            Integer employedItemsCount,                    // 지수가 채용한 종목 수

            @JsonProperty("clpr")
            Double closingPrice,                           // 종가: 정규 시장의 매매 시간 종료 시까지 형성되는 최종 가격

            @JsonProperty("vs")
            Double versus,                                 // 대비: 전일 대비 등락

            @JsonProperty("fltRt")
            Double fluctuationRate,                        // 등락률: 전일 대비 동락에 따른 비율

            @JsonProperty("mkp")
            Double marketOpeningPrice,                     // 시가: 정규 시장의 매매 시간 개시 후 형성되는 최초 가격

            @JsonProperty("hipr")
            Double highPrice,                              // 고가: 하루 중 지수의 최고치

            @JsonProperty("lopr")
            Double lowPrice,                               // 저가: 하루 중 지수의 최저치

            @JsonProperty("trqu")
            Long tradingVolume,                            // 거래량: 지수에 포함된 종목의 거래량 총합

            @JsonProperty("trPrc")
            Long tradingPrice,                             // 거래대금: 지수에 포함된 종목의 거래대금 총합

            @JsonProperty("lstgMrktTotAmt")
            Long listingMarketTotalAmount,                 // 지수에 포함된 종목의 시가 총액

            @JsonProperty("lsYrEdVsFltRg")
            Double lastYearEndVsFluctuationRange,          // 지수의 전년말대비 등락폭

            @JsonProperty("yrWRcrdHgst")
            Double yearlyRecordHighest,                    // 지수의 연중최고치

            @JsonProperty("yrWRcrdHgstDt")
            String yearlyRecordHighestDate,                // 지수의 연중최고치를 기록한 날짜

            @JsonProperty("yrWRcrdLwst")
            Double yearlyRecordLowest,                     // 지수의 연중최저치

            @JsonProperty("yrWRcrdLwstDt")
            String yearlyRecordLowestDate                  // 지수의 연중최저치를 기록한 날짜
    ) {}
}