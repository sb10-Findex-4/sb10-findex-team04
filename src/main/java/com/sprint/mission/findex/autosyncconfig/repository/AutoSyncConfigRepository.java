package com.sprint.mission.findex.autosyncconfig.repository;

import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// DB 접근 담당 Repository
public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Long>,
    JpaSpecificationExecutor<AutoSyncConfig> {

  // indexInfoId로 단건 조회
  @Query("select a from AutoSyncConfig a where a.indexInfo.id = :indexInfoId")
  Optional<AutoSyncConfig> findByIndexInfoId(@Param("indexInfoId") Long indexInfoId);

  // 자동 연동 활성화된 대상 조회 (N+1 방지)
  @EntityGraph(attributePaths = "indexInfo")
  List<AutoSyncConfig> findAllByEnabledTrue();

  // 대시보드용
  long countByEnabledTrue();
}
