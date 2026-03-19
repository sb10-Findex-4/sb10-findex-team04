package com.sprint.mission.findex.indexdata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "차트용 데이터 포인트")
public record ChartDataPoint(
    @Schema(description = "날짜", example = "2026-03-19")
    LocalDate date,     // 날짜

    @Schema(description = "지수 값", example = "2850.75")
    Double value        // 지수 값
) {}
