package com.sprint.mission.findex.indexdata.mapper;

import com.sprint.mission.findex.indexdata.dto.response.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexDataMapper {
    IndexData toEntity(IndexDataCreateRequestDto request);
    IndexDataDto toDto(IndexData indexData);
}
