package com.sprint.mission.findex.syncjob.repository;

import com.sprint.mission.findex.syncjob.entity.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, Long>, SyncJobRepositoryCustom {
}