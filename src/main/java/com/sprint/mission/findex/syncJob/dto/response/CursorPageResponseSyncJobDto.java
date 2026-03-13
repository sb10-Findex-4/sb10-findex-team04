package com.sprint.mission.findex.syncJob.dto.response;

import lombok.Builder;

import java.util.List;

/*
    연동 작업 페이지네이션 응답 DTO
 */

@Builder
public record CursorPageResponseSyncJobDto<T> (
        List<T> content,                    // 실제 데이터 목록
        String nextCursor,                  // 다음 페이지 커서
        Long nextIdAfter,                   // 이전 페이지의 마지막 요소 ID
        Integer size,                       // 페이지 크기
        Long totalElements,                  // 총 요소 개수
        Boolean hasNext                     // 다음 페이지 존재 여부
){
}
