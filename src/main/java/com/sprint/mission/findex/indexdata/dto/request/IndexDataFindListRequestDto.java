package com.sprint.mission.findex.indexdata.dto.request;

import java.time.LocalDate;

public record IndexDataFindListRequestDto(
        // 검색 필터 조건
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate,

        // 페이징 및 정렬
        Long idAfter,
        String cursor,
        String sortField,           // 기본값: baseDate
        String sortDirection,       // 기본값: desc
        Integer size                // 기본값: 10
) {

    public IndexDataFindListRequestDto {
        if (size == null)
            size = 10;

        if (sortDirection == null)
            sortDirection = "desc";

        if (sortField == null)
            sortField = "baseDate";
    }
}
