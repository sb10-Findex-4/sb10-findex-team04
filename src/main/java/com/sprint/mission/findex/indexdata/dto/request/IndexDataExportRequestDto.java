package com.sprint.mission.findex.indexdata.dto.request;

import java.time.LocalDate;

public record IndexDataExportRequestDto(
        // 검색 필터 조건
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate,

        // 정렬 조건
        String sortField,           // 기본값: baseDate
        String sortDirection        // 기본값: desc
) {
    public IndexDataExportRequestDto {
        if (sortDirection == null)
            sortDirection = "desc";

        if (sortField == null)
            sortField = "baseDate";
    }
}
