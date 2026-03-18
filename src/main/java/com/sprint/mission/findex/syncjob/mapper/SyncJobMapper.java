package com.sprint.mission.findex.syncjob.mapper;

import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SyncJobMapper {
    // 엔티티 -> 응답 DTO 변환
    @Mapping(target = "indexInfoId", source = "indexInfo.id")
    SyncJobDto toDto(SyncJob syncJob);
}
