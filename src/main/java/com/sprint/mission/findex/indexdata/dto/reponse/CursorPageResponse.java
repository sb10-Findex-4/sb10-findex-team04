package com.sprint.mission.findex.indexdata.dto.reponse;

import java.util.List;

public record CursorPageResponse<T> (
        List<T> contnet,
        Long nextCursor,
        int size,
        boolean hasNext
) {
}
