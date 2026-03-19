package com.sprint.mission.findex.syncjob.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "지수 데이터 연동 요청 DTO")
public record SyncJobIndexDataRequestDto(

    @Schema(description = "지수 정보 ID 목록", example = "1")
    @Nullable                       // null일 경우 전체 지수 대상
    List<Long> indexInfoIds,        // 지수 정보 ID 목록

    @Schema(description = "대상 날짜 (부터)", example = "2026-03-01")
    LocalDate baseDateFrom,         // 대상 날짜 (부터)

    @Schema(description = "대상 날짜 (까지)", example = "2026-03-18")
    LocalDate baseDateTo            // 대상 날짜 (까지)
) {
}
