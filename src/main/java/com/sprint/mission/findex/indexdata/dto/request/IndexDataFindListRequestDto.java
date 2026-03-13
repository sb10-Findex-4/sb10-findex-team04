package com.sprint.mission.findex.indexdata.dto.request;

import com.sprint.mission.findex.indexdata.entity.SortDirection;
import com.sprint.mission.findex.indexdata.entity.SortField;

import java.time.LocalDate;

public record IndexDataFindListRequestDto(
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate,
        Long idAfter,
        String cursor,
        SortField sortField,
        SortDirection sortDirection,
        Integer size
) {
}
