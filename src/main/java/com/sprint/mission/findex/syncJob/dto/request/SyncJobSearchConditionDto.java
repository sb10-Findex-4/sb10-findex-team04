package com.sprint.mission.findex.syncJob.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    연동 작업 목록 조회 요청 Dto
 */
public record SyncJobSearchConditionDto (
        String jobType,                 // 연동 작업 유형 (INDEX_INFO / INDEX_DATA)
        Long indexInfoId,               // 지수 정보 ID
        LocalDate baseDateFrom,         // 대상 날짜 (부터)
        LocalDate baseDateTo,           // 대상 날짜 (까지)
        String worker,                  // 작업자
        LocalDateTime jobTimeFrom,      // 작업 일시 (부터)
        LocalDateTime jobTimeTo,        // 작업 일시 (까지)
        String status,                  // 작업 상태 (SUCCESS / FAILED)
        Long idAfter,                   // 이전 페이지 마지막 요소 ID
        String cursor                   // 커서 (다음 페이지 시작점)
) {
}
