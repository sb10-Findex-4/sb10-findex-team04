package com.sprint.mission.findex.autosyncconfig.mapper;

import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// 엔티티와 DTO 간 변환을 담당하는 매퍼
@Mapper(componentModel = "spring")
public interface AutoSyncConfigMapper {

  @Mapping(target = "indexInfoId", source = "indexInfo.id")
  @Mapping(target = "indexClassification", source = "indexInfo.indexClassification")
  @Mapping(target = "indexName", source = "indexInfo.indexName")
  AutoSyncConfigDto toDto(AutoSyncConfig entity);
}