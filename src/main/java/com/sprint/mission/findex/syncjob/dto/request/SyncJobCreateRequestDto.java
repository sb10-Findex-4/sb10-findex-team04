package com.sprint.mission.findex.syncjob.dto.request;

import java.time.LocalDate;
import java.util.List;

public record SyncJobCreateRequestDto(
    String jobType,                 // 연동 작업 유형 (INDEX_INFO / INDEX_DATA)
    List<Long> indexInfoIds,        // 지수 정보 ID
    LocalDate baseDateFrom,         // 대상 날짜 (부터)
    LocalDate baseDateTo,           // 대상 날짜 (까지)
    String worker                   // 작업자
) {

}
