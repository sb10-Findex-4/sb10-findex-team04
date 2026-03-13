package com.sprint.mission.findex.syncJob.dto.response;

import java.util.List;

public record CursorPageResponseSyncJobDto<T> (
        List<T> content,                    // 실제 데이터 목록
        String nextCursor,                  // 다음 페이지 커서
        Long nextIdAfter,                   // 이전 페이지의 마지막 요소 ID
        Integer size,                       // 페이지 크기
        Long totalElement,                  // 총 요소 개수
        Boolean hsaNext                     // 다음 페이지 존재 여부
){
}
