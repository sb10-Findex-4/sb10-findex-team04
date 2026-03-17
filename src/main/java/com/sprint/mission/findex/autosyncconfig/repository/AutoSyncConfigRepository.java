package com.sprint.mission.findex.autosyncconfig.repository;

import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// DB접근 담당 Repo
public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Long>,
    JpaSpecificationExecutor<AutoSyncConfig> {
  /*
  TODO: 현재 팀 네이밍 기준으로 작성, 실제 연관관계 매핑 후 오류 시, 메서드명 수정 필요
   */
  Optional<AutoSyncConfig> findByIndexInfoId(Long indexInfoId);

  /*
 todo EntityGraph로 indexInfo를 함께 조회하여
   스케줄러 실행 시 추가 쿼리 발생을 방지하고 조회 성능을 개선하기 위해
   EntityGraph추가
   스케줄러 구현 단계에서 추후 구현 예정
  */
  @EntityGraph(attributePaths = "indexInfo")
  List<AutoSyncConfig> findAllByEnabledTrue();
}
