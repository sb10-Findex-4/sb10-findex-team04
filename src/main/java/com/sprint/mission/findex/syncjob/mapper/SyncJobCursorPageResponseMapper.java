package com.sprint.mission.findex.syncjob.mapper;

import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SyncJobCursorPageResponseMapper {
    // 응답 DTO -> 페이징 응답 DTO 반환
    default CursorPageResponseSyncJobDto<SyncJobDto> fromCursor(List<SyncJobDto> content,
                                                           String nextCursor,
                                                           Long nextIdAfter,
                                                           int size,
                                                           long totalElements,
                                                           boolean hasNext) {
        return CursorPageResponseSyncJobDto.<SyncJobDto>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(size)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .build();
    }
}
