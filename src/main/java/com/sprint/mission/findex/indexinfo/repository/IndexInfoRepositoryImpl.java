package com.sprint.mission.findex.indexinfo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sprint.mission.findex.indexinfo.entity.QIndexInfo.indexInfo;

// 동적 쿼리 실제 구현 Repo
@RequiredArgsConstructor
public class IndexInfoRepositoryImpl implements IndexInfoRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    // 지수
    @Override
    public List<IndexInfo> filter(String indexClassification, String indexName, Boolean favorite, Pageable pageable) {
        // 조건을 동적으로
        BooleanBuilder builder = new BooleanBuilder();

        // contains -> 지수 분류명 , 부분 일치 검색
        if (indexClassification != null && !indexClassification.isBlank()) {
            builder.and(indexInfo.indexClassification.contains(indexClassification));
        }

        // contains -> 지수명, 부분 일치 검색
        if (indexName != null && !indexName.isBlank()) {
            builder.and(indexInfo.indexName.contains(indexName));
        }

        // eq -> 즐겨찾기 완전 일치 검색
        if (favorite != null) {
            builder.and(indexInfo.favorite.eq(favorite));
        }

        OrderSpecifier<?> orderSpecifier = indexInfo.indexClassification.asc();

//        if ("indexName".equals(sortField)) {
//            orderSpecifier = indexInfo.indexName.asc();
//        } else if ("employedItemsCount".equals(sortField)) {
//            orderSpecifier = indexInfo.employedItemsCount.asc();
//        }

        return jpaQueryFactory
                .selectFrom(indexInfo)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
