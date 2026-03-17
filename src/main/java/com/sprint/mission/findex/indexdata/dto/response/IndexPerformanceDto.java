package com.sprint.mission.findex.indexdata.dto.response;

public record IndexPerformanceDto (
    Long indexInfoId,                 // 지수 정보 ID
    String indexClassification,       // 지수 구분
    String indexName,                 // 지수 이름
    Double versus,                    // 대비 (전일 대비 차이 금액)
    Double fluctuationRate,           // 등략률
    Double currentPrice,              // 현재가
    Double beforePrice                // 기준가 (이전 가격)
) {}
