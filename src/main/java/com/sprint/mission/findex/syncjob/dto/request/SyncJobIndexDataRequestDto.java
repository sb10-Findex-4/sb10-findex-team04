package com.sprint.mission.findex.syncjob.dto.request;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record SyncJobIndexDataRequestDto(
        List<Long> indexInfoIds,        // 지수 정보 ID목록
        LocalDate baseDateFrom,         // 대상 날짜(부터)
        LocalDate baseDateTo            // 대상 날짜(까지)
) {
}
