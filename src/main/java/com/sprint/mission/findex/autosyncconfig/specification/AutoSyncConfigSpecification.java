package com.sprint.mission.findex.autosyncconfig.specification;

import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import org.springframework.data.jpa.domain.Specification;

// 자동 연동 설정 목록 조회 시 필터링 조건(indexInfoId, enabled)을 처리하는 Specification
public class AutoSyncConfigSpecification {

  // 외부에서 new로 생성하지 못하도록 생성자를 private로 막는다.
  private AutoSyncConfigSpecification() {
  }

  /*
   * indexInfoId로 자동 연동 설정을 필터링
   * indexInfo.id = 3 인 자동연동 설정만 조회
   */
  public static Specification<AutoSyncConfig> hasIndexInfoId(Long indexInfoId) {

    return (root, query, criteriaBuilder) ->

        // indexInfoId가 null이면 필터 조건을 추가하지 않음 → 전체 조회
        indexInfoId == null
            ? null
            // indexInfoId가 존재하면 indexInfo.id = indexInfoId 조건 생성
            : criteriaBuilder.equal(
                root.get("indexInfo").get("id"),
                indexInfoId
            );
  }

  /*
   * enabled 값으로 자동 연동 설정을 필터링
   * → enabled = true 인 설정만 조회
   */
  public static Specification<AutoSyncConfig> hasEnabled(Boolean enabled) {

    return (root, query, criteriaBuilder) ->

        // enabled 값이 null이면 해당 조건은 조회에 포함하지 않음
        enabled == null
            ? null
            // enabled 값이 존재하면 enabled = 요청값 조건 생성
            : criteriaBuilder.equal(
                root.get("enabled"),
                enabled
            );
  }
}
