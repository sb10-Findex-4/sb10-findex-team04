package com.sprint.mission.findex.indexdata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "지수 차트 조회 응답 DTO")
public record IndexChartDto(
    @Schema(description = "지수 정보 고유 ID", example = "1")
    Long indexInfoId,                     // 지수 정보 ID

    @Schema(description = "지수 구분", example = "KOSPI시리즈")
    String indexClassification,           // 지수 구분

    @Schema(description = "지수 이름", example = "IT 서비스")
    String indexName,                     // 지수 이름

    @Schema(description = "조회 기간 (1달, 3달, 1년)", example = "1달")
    String periodType,                    // 조회 기간

    @Schema(description = "실제 지수 데이터")
    List<IndexDataDto> dataPoints,      // 실제 지수 데이터

    @Schema(description = "5일 이동평균선 데이터")
    List<IndexDataDto> ma5DataPoints,   // 5일 이동평균선 데이터

    @Schema(description = "20일 이동평균선 데이터")
    List<IndexDataDto> ma20DataPoints   // 20일 이동평균선 데이터
) {}
