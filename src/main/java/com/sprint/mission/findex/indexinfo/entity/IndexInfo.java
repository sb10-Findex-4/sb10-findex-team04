package com.sprint.mission.findex.indexinfo.entity;

import com.sprint.mission.findex.base.BaseEntity;
import com.sprint.mission.findex.indexinfo.SourceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
지수 정보 데이터
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "index_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_index_classification_index_name",
                        columnNames = {"index_classification", "index_name"}
                )
        }
)
public class IndexInfo extends BaseEntity {
    // 지수 분류명
    @Column(name = "index_classification", length = 100, nullable = false)
    private String indexClassification;
    // 지수 이름
    @Column(name = "index_name", length = 100, nullable = false)
    private String indexName;
    // 채용 종목 수
    @Column(name = "employed_items_count")
    private Integer employedItemsCount;
    // 기준 시점
    @Column(name = "base_point_in_time")
    private LocalDate basePointInTime;
    // 기준 지수
    @Column(name = "base_index", precision = 15, scale = 2)
    private BigDecimal baseIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;
    // 즐겨찾기
    @Column(name = "favorite", nullable = false)
    private boolean favorite;
}
