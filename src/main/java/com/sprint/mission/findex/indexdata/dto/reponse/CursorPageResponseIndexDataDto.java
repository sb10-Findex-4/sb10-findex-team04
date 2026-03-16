package com.sprint.mission.findex.indexdata.dto.reponse;

import lombok.Builder;
import java.util.List;

// TODO
@Builder
public record CursorPageResponseIndexDataDto<T> (
        List<T> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
}
