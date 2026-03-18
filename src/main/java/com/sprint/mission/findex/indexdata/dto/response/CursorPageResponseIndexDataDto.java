package com.sprint.mission.findex.indexdata.dto.response;

import lombok.Builder;
import java.util.List;

// TODO
@Builder
public record CursorPageResponseIndexDataDto<T> (
        List<T> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        int totalElements,
        Boolean hasNext
) {
}
