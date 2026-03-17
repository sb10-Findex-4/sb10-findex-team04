package com.sprint.mission.findex.syncjob.dto.request;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record SyncJobIndexDataRequestDto(
        List<Long> indexInfoIds,
        LocalDate baseDateFrom,
        LocalDate baseDateTo
) {
}
