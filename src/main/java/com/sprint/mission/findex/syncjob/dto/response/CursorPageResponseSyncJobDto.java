package com.sprint.mission.findex.syncjob.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/*
    연동 작업 페이지네이션 응답 DTO
 */

@Builder
@Schema(description = "연동 작업 페이지네이션 응답 DTO")
public record CursorPageResponseSyncJobDto<T>(
    @Schema(description = "실제 데이터 목록")
    List<SyncJobDto> content,           // 실제 데이터 목록

    @Schema(description = "다음 페이지 시작점 커서", example = "Y3Vyc29y")
    String nextCursor,                  // 다음 페이지 커서

    @Schema(description = "다음 페이지 조회 시 사용할 마지막 ID", example = "105")
    Long nextIdAfter,                   // 이전 페이지의 마지막 요소 ID

    @Schema(description = "요청한 페이지 크기", example = "10")
    Integer size,                       // 페이지 크기

    @Schema(description = "총 요소 개수", example = "150")
    Long totalElements,                  // 총 요소 개수

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    Boolean hasNext                     // 다음 페이지 존재 여부
) {

}
