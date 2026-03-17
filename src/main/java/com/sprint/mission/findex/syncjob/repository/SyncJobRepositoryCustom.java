package com.sprint.mission.findex.syncjob.repository;

import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;

import java.util.List;

/*
    Query DSL 적용을 위한 Custom Repository Interface
 */
public interface SyncJobRepositoryCustom {
    // 필터링 + 정렬 + 커서 기반 페이지네이션이 적용된 연동 목록 조회
    List<SyncJob> searchSyncJobs(SyncJobSearchConditionDto condition);

    // 필터링 조건이 적용된 연동 목록의 전체 개수 조회
    long countWithFilter(SyncJobSearchConditionDto condition);
}
