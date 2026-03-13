package com.sprint.mission.findex.indexinfo.mapper;

import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

// TODO: 완성되면 IGNORE -> WARN, ERROR로 변경
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favorite", constant = "false")
    IndexInfo toEntity(IndexInfoCreateRequestDto request);

    IndexInfoDto toDto(IndexInfo indexInfo);
}
