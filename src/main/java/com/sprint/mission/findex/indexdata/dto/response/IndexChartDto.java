package com.sprint.mission.findex.indexdata.dto.response;

import java.util.List;

public record IndexChartDto(
    Long indexInfoId,                     // 지수 정보 ID
    String indexClassification,           // 지수 구분
    String indexName,                     // 지수 이름
    String periodType,                    // 조회 기간
    List<ChartDataPoint> dataPoints,      // 실제 지수 데이터
    List<ChartDataPoint> ma5DataPoints,   // 5일 이동평균선 데이터
    List<ChartDataPoint> ma20DataPoints   // 20일 이동평균선 데이터
) {}
