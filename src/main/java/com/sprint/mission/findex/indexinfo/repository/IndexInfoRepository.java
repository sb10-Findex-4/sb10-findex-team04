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
@RequiredArgsConstructor
public abstract class IndexInfoRepository implements IndexInfoRepositoryCustom, JpaRepository<IndexInfo, Long> {
    private final JPAQueryFactory jpaQueryFactory;

    // (지수 분류명, 지수 정보명) 중복 검사
    @Override
    public boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName) {
        return false;
    }

    // 지수
    @Override
    public List<IndexInfo> filter(String indexClassification, String indexName, Boolean favorite, Pageable pageable) {
        return List.of();
    }
}
