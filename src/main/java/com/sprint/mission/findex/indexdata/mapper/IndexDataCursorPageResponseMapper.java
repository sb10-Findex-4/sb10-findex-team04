package com.sprint.mission.findex.indexdata.mapper;

import com.sprint.mission.findex.indexdata.dto.response.CursorPageResponseIndexDataDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IndexDataCursorPageResponseMapper {
    // 응답 DTO -> 페이징 응답 DTO 반환
    default CursorPageResponseIndexDataDto<IndexDataDto> fromCursor(List<IndexDataDto> content,
                                                             String nextCursor,
                                                             Long nextIdAfter,
                                                             int size,
                                                             int totalElements,
                                                             boolean hasNext) {
        return CursorPageResponseIndexDataDto.<IndexDataDto>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(size)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .totalElements(totalElements)
                .build();
    }
}
