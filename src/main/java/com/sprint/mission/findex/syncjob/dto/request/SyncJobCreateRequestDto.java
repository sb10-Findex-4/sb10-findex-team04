package com.sprint.mission.findex.syncjob.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "연동 작업 생성 요청 DTO")
public record SyncJobCreateRequestDto(
    @Schema(description = "연동 작업 유형", example = "INDEX_DATA", allowableValues = {"INDEX_INFO", "INDEX_DATA"})
    String jobType,                 // 연동 작업 유형 (INDEX_INFO / INDEX_DATA)

    @Schema(description = "지수 정보 ID", example = "1")
    List<Long> indexInfoIds,        // 지수 정보 ID

    @Schema(description = "대상 날짜 (부터)", example = "2026-03-01")
    LocalDate baseDateFrom,         // 대상 날짜 (부터)


    @Schema(description = "대상 날짜 (까지)", example = "2026-03-18")
    LocalDate baseDateTo,           // 대상 날짜 (까지)

    @Schema(description = "작업자", example = "127.0.0.1")
    String worker                   // 작업자
) {

}
