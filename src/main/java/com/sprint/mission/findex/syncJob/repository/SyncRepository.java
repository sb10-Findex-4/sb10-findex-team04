package com.sprint.mission.findex.syncJob.repository;

import com.sprint.mission.findex.syncJob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncJob.entity.SyncJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SyncRepository extends CrudRepository<SyncJob, Long> {
    /*
    연동 작업 목록 페이지네이션 조회
    첫 페이지의 경우, 커서가 존재하지 않기 때문에 커서가 있는 / 없는 경우를 별도로 선언
     */

    // 커서가 없는 연동 작업 목록 조회
    List<SyncJob> findFirstPageSyncJobs(SyncJobSearchConditionDto syncJobSearchConditionDto, Pageable pageable);

    // 커서가 있는 연동 작업 목록 조회
    List<SyncJob> findNextPageSyncJobsById(SyncJobSearchConditionDto syncJobSearchConditionDto, Long idAfter, Pageable pageable);

    // 연동 작업의 전체 개수
    long countWithFilter(SyncJobSearchConditionDto condition);
}
