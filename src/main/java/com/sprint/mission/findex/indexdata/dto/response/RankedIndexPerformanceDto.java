package com.sprint.mission.findex.indexdata.dto.response;

public record RankedIndexPerformanceDto (
    IndexPerformanceDto performance, // 성과 객체
    Integer rank                     // 순위
) {}
