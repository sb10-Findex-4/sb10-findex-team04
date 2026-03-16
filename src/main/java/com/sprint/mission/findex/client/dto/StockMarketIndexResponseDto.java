package com.sprint.mission.findex.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*
    공공데이터포털 (금융위원회) API의 응답 구조
    -------------------------
    API 규격의 약자(basDt 등)를 @JsonProperty로 매핑
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
            @JsonProperty("basDt")
            String baseDate,                          // 기준일자

            @JsonProperty("idxNm")
            String indexName,                         // 지수명

            @JsonProperty("clpr")
            Double closingPrice,                      // 종가: 장이 끝날 때의 가격

            @JsonProperty("vs")
            Double versus,                            // 대비: 전일 대비 등락 수치

            @JsonProperty("fltRt")
            Double fluctuationRate,                   // 등락률: 가격 변동 비율 (%)

            @JsonProperty("mkp")
            Double openingPrice,                      // 시가: 장이 시작할 때의 가격

            @JsonProperty("hipr")
            Double highPrice,                         // 고가: 당일 가장 높았던 가격

            @JsonProperty("lopr")
            Double lowPrice,                          // 저가: 당일 가장 낮았던 가격

            @JsonProperty("trqu")
            Long tradingVolume,                       // 거래량: 매매된 주식의 수량

            @JsonProperty("trPrc")
            Long tradingPrice                         // 거래대금: 매매된 주식의 총 금액
    ) {}
}