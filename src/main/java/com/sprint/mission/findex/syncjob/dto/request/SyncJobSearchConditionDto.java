package com.sprint.mission.findex.syncjob.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    연동 작업 목록 조회 요청 Dto
 */
public record SyncJobSearchConditionDto (
        // 1. 검색 필터 조건
        String jobType,                 // 연동 작업 유형 (INDEX_INFO / INDEX_DATA)
        Long indexInfoId,               // 지수 정보 ID
        LocalDate baseDateFrom,         // 대상 날짜 (부터)
        LocalDate baseDateTo,           // 대상 날짜 (까지)
        String worker,                  // 작업자
        LocalDateTime jobTimeFrom,      // 작업 일시 (부터)
        LocalDateTime jobTimeTo,        // 작업 일시 (까지)
        String status,                  // 작업 상태 (SUCCESS / FAILED)

        // 2. 페이징 및 정렬
        Long idAfter,                   // 이전 페이지 마지막 요소 ID
        String cursor,                  // 커서 (다음 페이지 시작점)
        Integer size,                   // 페이지 크기 (기본값: 10)
        String sortField,               // 정렬 기준 (기본값: jobTime)
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
