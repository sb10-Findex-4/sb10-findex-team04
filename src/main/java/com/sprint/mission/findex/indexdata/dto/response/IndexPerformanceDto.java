package com.sprint.mission.findex.indexdata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지수 성과 정보 DTO")
public record IndexPerformanceDto (
    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,                 // 지수 정보 ID

    @Schema(description = "지수 구분", example = "KOSPI시리즈")
    String indexClassification,       // 지수 구분

    @Schema(description = "지수 이름", example = "IT 서비스")
    String indexName,                 // 지수 이름

    @Schema(description = "전일 대비 등락폭", example = "50.5")
    Double versus,                    // 대비 (전일 대비 차이 금액)

    @Schema(description = "전일 대비 등락률", example = "1.8")
    Double fluctuationRate,           // 등략률

    @Schema(description = "현재가", example = "2850.75")
    Double currentPrice,              // 현재가

    @Schema(description = "기준가", example = "2850.75")
    Double beforePrice                // 기준가 (이전 가격)
) {}
