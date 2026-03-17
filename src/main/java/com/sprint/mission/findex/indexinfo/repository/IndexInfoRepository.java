package com.sprint.mission.findex.indexinfo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>, IndexInfoRepositoryCustom {
    // (IndexClassification, IndexName) 중복 검사 메서드
    boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);

    //
    IndexInfo findByIndexClassificationAndIndexName(String indexClassification, String indexName);
}
