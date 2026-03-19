package com.sprint.mission.findex.indexdata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "순위가 포함된 지수 성과 DTO")
public record RankedIndexPerformanceDto (
    @Schema(description = "지수 성과 객체")
    IndexPerformanceDto performance, // 성과 객체

    @Schema(description = "성과 순위", example = "1")
    Integer rank                     // 순위
) {}
