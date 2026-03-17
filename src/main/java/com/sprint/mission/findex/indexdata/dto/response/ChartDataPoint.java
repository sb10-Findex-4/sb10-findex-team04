package com.sprint.mission.findex.indexdata.dto.response;

import java.time.LocalDate;

public record ChartDataPoint(
    LocalDate date,     // 날짜
    Double value        // 지수 값
) {}
