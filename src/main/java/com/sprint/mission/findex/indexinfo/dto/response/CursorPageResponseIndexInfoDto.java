package com.sprint.mission.findex.indexinfo.dto.response;

import java.util.List;
/*
커서 기반 페이지 응답 Dto
 */
public record CursorPageResponseIndexInfoDto(
        List<IndexInfoDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        int totalElements,
        boolean hasNext
) {
}
