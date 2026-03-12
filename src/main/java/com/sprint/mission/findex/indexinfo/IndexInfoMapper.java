package com.sprint.mission.findex.indexinfo;

import com.sprint.mission.findex.indexinfo.dto.IndexInfoCreateRequest;
import com.sprint.mission.findex.indexinfo.dto.IndexInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favorite", constant = "false")
    IndexInfo toEntity(IndexInfoCreateRequest request);

    IndexInfoDto toDto(IndexInfo indexInfo);
}
