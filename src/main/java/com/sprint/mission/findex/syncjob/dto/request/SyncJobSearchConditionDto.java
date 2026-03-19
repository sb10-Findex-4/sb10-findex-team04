package com.sprint.mission.findex.syncjob.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    연동 작업 목록 조회 요청 Dto
 */
@Schema(description = "연동 작업 목록 조회 요청 DTO")
public record SyncJobSearchConditionDto (
        // 1. 검색 필터 조건
        @Schema(description = "연동 작업 유형", example = "INDEX_DATA", allowableValues = {"INDEX_INFO", "INDEX_DATA"})
        String jobType,                 // 연동 작업 유형 (INDEX_INFO / INDEX_DATA)

        @Schema(description = "지수 정보 ID", example = "1")
        Long indexInfoId,               // 지수 정보 ID

        @Schema(description = "대상 날짜 (부터)", example = "2026-03-01")
        LocalDate baseDateFrom,         // 대상 날짜 (부터)

        @Schema(description = "대상 날짜 (까지)", example = "2026-03-18")
        LocalDate baseDateTo,           // 대상 날짜 (까지)

        @Schema(description = "작업자", example = "127.0.0.1")
        String worker,                  // 작업자

        @Schema(description = "작업 일시 (부터)", example = "2026-03-19T00:00:00")
        LocalDateTime jobTimeFrom,      // 작업 일시 (부터)

        @Schema(description = "작업 일시 (까지)", example = "2026-03-19T23:59:59")
        LocalDateTime jobTimeTo,        // 작업 일시 (까지)

        @Schema(description = "작업 결과 상태", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED"})
        String status,                  // 작업 상태 (SUCCESS / FAILED)


        // 2. 페이징 및 정렬
        @Schema(description = "이전 페이지 마지막 요소 ID", example = "100")
        Long idAfter,                   // 이전 페이지 마지막 요소 ID

        @Schema(description = "다음 페이지 시작점 커서", example = "Y3Vyc29y")
        String cursor,                  // 커서 (다음 페이지 시작점)

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        Integer size,                   // 페이지 크기 (기본값: 10)

        @Schema(description = "정렬 기준", example = "jobTime", defaultValue = "jobTime")
        String sortField,               // 정렬 기준 (기본값: jobTime)

        @Schema(description = "정렬 방향", example = "DESC", allowableValues = {"ASC", "DESC"}, defaultValue = "DESC")
        String sortDirection            // 정렬 방향 (ASC / DESC, 기본값: desc)
) {
    // 기본값 설정을 위한 생성자
    public SyncJobSearchConditionDto {
        if (size == null)
            size = 10;

        if (sortField == null)
            sortField = "jobTime";

        if (sortDirection == null)
            sortDirection = "desc";
    }
}
