package com.sprint.mission.findex.indexdata.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.sprint.mission.findex.indexdata.entity.QIndexData.indexData;

@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<IndexData> filter(IndexDataFindListRequestDto request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.indexInfoId() != null) {
            builder.and(indexData.indexInfoId.eq(request.indexInfoId()));
        }
        if (request.startDate() != null) {
            builder.and(indexData.baseDate.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            builder.and(indexData.baseDate.loe(request.endDate()));
        }

        boolean isAsc = "asc".equalsIgnoreCase(request.sortDirection());

        // 커서 조건 (baseDate + id tie-break)
        if (request.cursor() != null && request.idAfter() != null) {
            LocalDate cursorDate = LocalDate.parse(request.cursor());

            BooleanBuilder cursorBuilder = new BooleanBuilder();
            if (isAsc) {
                cursorBuilder.or(indexData.baseDate.gt(cursorDate));
                cursorBuilder.or(indexData.baseDate.eq(cursorDate).and(indexData.id.gt(request.idAfter())));
            } else {
                cursorBuilder.or(indexData.baseDate.lt(cursorDate));
                cursorBuilder.or(indexData.baseDate.eq(cursorDate).and(indexData.id.lt(request.idAfter())));
            }

            builder.and(cursorBuilder);
        }

        OrderSpecifier<?> dateOrder = isAsc ? indexData.baseDate.asc() : indexData.baseDate.desc();
        OrderSpecifier<?> idOrder = isAsc ? indexData.id.asc() : indexData.id.desc();

        int size = request.size() != null ? request.size() : 10;

        return jpaQueryFactory
                .selectFrom(indexData)
                .where(builder)
                .orderBy(dateOrder, idOrder)
                .limit(size + 1L)
                .fetch();
    }

    @Override
    public int count(IndexDataFindListRequestDto request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.indexInfoId() != null) {
            builder.and(indexData.indexInfoId.eq(request.indexInfoId()));
        }
        if (request.startDate() != null) {
            builder.and(indexData.baseDate.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            builder.and(indexData.baseDate.loe(request.endDate()));
        }

        Integer result = jpaQueryFactory
                .select(indexData.count().intValue())
                .from(indexData)
                .where(builder)
                .fetchOne();

        return result != null ? result : 0;
    }

    @Override
    public List<IndexData> exportFilter(IndexDataExportRequestDto request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.indexInfoId() != null) {
            builder.and(indexData.indexInfoId.eq(request.indexInfoId()));
        }
        if (request.startDate() != null) {
            builder.and(indexData.baseDate.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            builder.and(indexData.baseDate.loe(request.endDate()));
        }

        boolean isAsc = "asc".equalsIgnoreCase(request.sortDirection());

        OrderSpecifier<?> sortOrder = switch(request.sortField()) {
            case "marketPrice" -> isAsc ? indexData.marketPrice.asc() : indexData.marketPrice.desc();
            case "closingPrice" -> isAsc ? indexData.closingPrice.asc() : indexData.closingPrice.desc();
            case "highPrice" -> isAsc ? indexData.highPrice.asc() : indexData.highPrice.desc();
            case "lowPrice" -> isAsc ? indexData.lowPrice.asc() : indexData.lowPrice.desc();
            case "versus" -> isAsc ? indexData.versus.asc() : indexData.versus.desc();
            case "fluctuationRate" -> isAsc ? indexData.fluctuationRate.asc() : indexData.fluctuationRate.desc();
            case "tradingQuantity" -> isAsc ? indexData.tradingQuantity.asc() : indexData.tradingQuantity.desc();
            case "tradingPrice" -> isAsc ? indexData.tradingPrice.asc() : indexData.tradingPrice.desc();
            case "marketTotalAmount" -> isAsc ? indexData.marketTotalAmount.asc() : indexData.marketTotalAmount.desc();
            default -> isAsc ? indexData.baseDate.asc() : indexData.baseDate.desc();
        };

        OrderSpecifier<?> idOrder = isAsc ? indexData.id.asc() : indexData.id.desc();

        return jpaQueryFactory
                .selectFrom(indexData)
                .where(builder)
                .orderBy(sortOrder, idOrder)
                .fetch();
    }
}
