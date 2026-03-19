package com.sprint.mission.findex.autosyncconfig.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 커서 기반 페이지 응답 DTO
@Schema(description = "커서 기반 페이지 응답 DTO")
public record CursorPageResponseAutoSyncConfigDto (
        @Schema(description = "페이지 내용", implementation = AutoSyncConfigDto.class)
        List<AutoSyncConfigDto> content,

        @Schema(description = "다음 페이지 커서", example = "eyJpZCI6MjB9")
        String nextCursor,

        @Schema(description = "마지막 요소의 ID", example = "eyJpZCI6MjB9")
        Long nextIdAfter,

        @Schema(description = "페이지 크기", example = "10")
        int size,

        @Schema(description = "총 요소 수", example = "100")
        long totalElements,

        @Schema(description = "다음 페이지 여부", example = "true")
        boolean hasNext
) {
}
