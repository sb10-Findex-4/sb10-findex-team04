package com.sprint.mission.findex.syncjob.mapper;

import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SyncJobMapper {
    // 엔티티 -> 응답 DTO 변환
    SyncJobDto toDto(SyncJob syncJob);
}
