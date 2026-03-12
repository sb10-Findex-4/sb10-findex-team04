package com.sprint.mission.findex.indexinfo.repository;

import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
    // (IndexClassification, IndexName) 중복 검사
    boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
}
