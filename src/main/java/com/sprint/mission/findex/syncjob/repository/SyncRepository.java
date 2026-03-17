package com.sprint.mission.findex.syncjob.repository;

import com.sprint.mission.findex.syncjob.entity.SyncJob;
import org.springframework.data.repository.CrudRepository;

public interface SyncRepository extends CrudRepository<SyncJob, Long>, SyncJobRepositoryCustom {
}
