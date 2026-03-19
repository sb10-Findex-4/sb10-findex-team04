package com.sprint.mission.findex.indexdata.dto.response;

import com.sprint.mission.findex.indexdata.entity.PeriodType;

import java.util.List;

public record IndexChartDto(
        Long indexInfoId,                     // 지수 정보 ID
        String indexClassification,           // 지수 구분
        String indexName,                     // 지수 이름
        PeriodType periodType,                    // 조회 기간
        List<Object> dataPoints,      // 실제 지수 데이터
        List<Object> ma5DataPoints,   // 5일 이동평균선 데이터
        List<Object> ma20DataPoints   // 20일 이동평균선 데이터
) {}
