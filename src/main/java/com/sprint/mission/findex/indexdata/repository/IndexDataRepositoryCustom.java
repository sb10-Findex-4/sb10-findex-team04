package com.sprint.mission.findex.indexdata.repository;

import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;

import java.util.List;

public interface IndexDataRepositoryCustom {
    List<IndexData> filter(IndexDataFindListRequestDto request);
    int count(IndexDataFindListRequestDto request);
    List<IndexData> exportFilter(IndexDataExportRequestDto request);
}
