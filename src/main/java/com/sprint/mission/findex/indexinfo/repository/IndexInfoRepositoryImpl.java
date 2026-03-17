package com.sprint.mission.findex.indexinfo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.sprint.mission.findex.indexinfo.entity.QIndexInfo.indexInfo;

// 동적 쿼리 실제 구현 Repo
@RequiredArgsConstructor
public class IndexInfoRepositoryImpl implements IndexInfoRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    // 필터링 + 정렬 + 페이지네이션
    @Override
    public List<IndexInfo> filter(IndexInfoSearchRequestDto request) {
        // 동적 조건을 추가해 나감
        BooleanBuilder builder = new BooleanBuilder();

        // 지수 분류명 부분 일치 조건 추가
        if (request.indexClassification() != null && !request.indexClassification().isBlank()) {
            builder.and(indexInfo.indexClassification.contains(request.indexClassification()));
        }

        // 지수명 부분 일치 조건 추가
        if (request.indexName() != null && !request.indexName().isBlank()) {
            builder.and(indexInfo.indexName.contains(request.indexName()));
        }

        // 즐겨찾기 완전 일치 조건 추가
        if (request.favorite() != null) {
            builder.and(indexInfo.favorite.eq(request.favorite()));
        }

        // 정렬 방향 (기본값 asc)
        boolean isAsc = "asc".equalsIgnoreCase(request.sortDirection());

        // 커서 페이지네이션 조건 - 커서가 존재할 경우에만 이전 페이지 마지막 요소 이후부터 조회
        if (request.cursor() != null) {
            BooleanBuilder cursorBuilder = new BooleanBuilder();

            switch (request.sortField()) {
                case "indexName" -> {
                    // asc면 마지막 값보다 큰 것, desc면 마지막 값보다 작은 것
                    cursorBuilder.or(isAsc
                            ? indexInfo.indexName.gt(request.cursor())
                            : indexInfo.indexName.lt(request.cursor()));

                    // 같은 indexName 값이 있을 경우 id로 2차 커서 처리
                    if (request.idAfter() != null) {
                        cursorBuilder.or(indexInfo.indexName.eq(request.cursor())
                                .and(isAsc
                                        ? indexInfo.id.gt(request.idAfter())
                                        : indexInfo.id.lt(request.idAfter())));
                    }
                }
                case "employedItemsCount" -> {
                    // cursor는 String이라 Int으로 변환
                    Integer lastCount = Integer.parseInt(request.cursor());

                    // asc면 마지막 값보다 큰 것, desc면 마지막 값보다 작은 것
                    cursorBuilder.or(isAsc
                            ? indexInfo.employedItemsCount.gt(lastCount)
                            : indexInfo.employedItemsCount.lt(lastCount));

                    // 같은 employedItemsCount 값이 있을 경우 id로 2차 커서 처리
                    if (request.idAfter() != null) {
                        cursorBuilder.or(indexInfo.employedItemsCount.eq(lastCount)
                                .and(isAsc
                                        ? indexInfo.id.gt(request.idAfter())
                                        : indexInfo.id.lt(request.idAfter())));
                    }
                }
                default -> {
                    // 기본 정렬 기준: indexClassification
                    cursorBuilder.or(isAsc
                            ? indexInfo.indexClassification.gt(request.cursor())
                            : indexInfo.indexClassification.lt(request.cursor()));

                    // 같은 indexClassification 값이 있을 경우 id로 2차 커서 처리
                    if (request.idAfter() != null) {
                        cursorBuilder.or(indexInfo.indexClassification.eq(request.cursor())
                                .and(isAsc
                                        ? indexInfo.id.gt(request.idAfter())
                                        : indexInfo.id.lt(request.idAfter())));
                    }
                }
            }
            // builder 조건 + cursorBuilder 조건
            builder.and(cursorBuilder);
        }

        // 정렬 조건 - sortField, sortDirection 기준으로 동적 정렬
        OrderSpecifier<?> orderSpecifier = switch (request.sortField()) {
            case "indexName" -> isAsc
                    ? indexInfo.indexName.asc()
                    : indexInfo.indexName.desc();
            case "employedItemsCount" -> isAsc
                    ? indexInfo.employedItemsCount.asc()
                    : indexInfo.employedItemsCount.desc();
            default -> isAsc
                    ? indexInfo.indexClassification.asc()
                    : indexInfo.indexClassification.desc();
        };

        return jpaQueryFactory
                .selectFrom(indexInfo)
                .where(builder) // 조건 추가
                .orderBy(
                        orderSpecifier,
                        isAsc ? indexInfo.id.asc() : indexInfo.id.desc()
                ) // 동일 값일 경우 id 기준 정렬
                .limit(request.size() + 1) // hasNext 판단을 위해 size + 1 조회
                .fetch(); // 리스트로 변환
    }

    @Override
    public int count(IndexInfoSearchRequestDto request) {
        BooleanBuilder builder = new BooleanBuilder();

        // 커서 조건 제외하고 필터 조건만으로 전체 개수 조회
        if (request.indexClassification() != null && !request.indexClassification().isBlank())
            builder.and(indexInfo.indexClassification.contains(request.indexClassification()));
        if (request.indexName() != null && !request.indexName().isBlank())
            builder.and(indexInfo.indexName.contains(request.indexName()));
        if (request.favorite() != null)
            builder.and(indexInfo.favorite.eq(request.favorite()));

        Integer result = jpaQueryFactory
                .select(indexInfo.count().intValue()) // COUNT Long -> Int 변환
                .from(indexInfo)
                .where(builder) // 필터 조건 적용
                .fetchOne(); // 단건 반환

        // fetchOne이 null일 경우에는 0을 반환
        return result != null ? result : 0;
    }
}