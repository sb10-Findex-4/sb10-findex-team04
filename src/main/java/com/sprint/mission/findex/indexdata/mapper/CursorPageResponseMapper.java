package com.sprint.mission.findex.indexdata.mapper;

import com.sprint.mission.findex.indexdata.dto.reponse.CursorPageResponseIndexDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CursorPageResponseMapper {
    // 응답 DTO -> 페이징 응답 DTO 반환
    default <T> CursorPageResponseIndexDataDto<T> fromCursor(List<T> content,
                                                             String nextCursor,
                                                             Long nextIdAfter,
                                                             int size,
                                                             boolean hasNext) {
        return CursorPageResponseIndexDataDto.<T>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(size)
                .hasNext(hasNext)
                .build();
    }
}
