package com.sprint.mission.findex.syncJob.mapper;

import com.sprint.mission.findex.syncJob.dto.response.CursorPageResponseSyncJobDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CursorPageResponseMapper {
    // 응답 DTO -> 페이징 응답 DTO 반환
    default <T> CursorPageResponseSyncJobDto<T> fromCursor(List<T> content,
                                                           String nextCursor,
                                                           Long nextIdAfter,
                                                           int size,
                                                           long totalElements,
                                                           boolean hasNext) {
        return CursorPageResponseSyncJobDto.<T>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(size)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .build();
    }
}
