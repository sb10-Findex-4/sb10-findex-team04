package com.sprint.mission.findex.indexinfo.repository;

import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import org.springframework.data.domain.Pageable;
import java.util.List;

/*
 * 지수 정보 필터링,페이지네이션, 중복 검사를 위한 커스텀 Repository 인터페이스
 */
public interface IndexInfoRepositoryCustom {
    // (IndexClassification, IndexName) 중복 검사 메서드
    boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);

    // 필터링 + 페이지네이션
    List<IndexInfo> filter(String indexClassification, String indexName, Boolean favorite, Pageable pageable);
}
