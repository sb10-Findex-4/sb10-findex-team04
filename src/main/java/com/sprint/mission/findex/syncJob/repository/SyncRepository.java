package com.sprint.mission.findex.syncJob.repository;

import com.sprint.mission.findex.syncJob.entity.SyncJob;
import org.springframework.data.repository.CrudRepository;

public interface SyncRepository extends CrudRepository<SyncJob, Long> {
}
