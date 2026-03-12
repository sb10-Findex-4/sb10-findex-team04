package com.sprint.mission.findex.indexinfo;

import com.sprint.mission.findex.base.BaseEntity;
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
@Table(name = "index_info")
public class IndexInfo extends BaseEntity {

    @Column(name = "index_classification", length = 100, nullable = false)
    private String indexClassification;

    @Column(name = "index_name", length = 100, nullable = false)
    private String indexName;

    @Column(name = "employed_items_count")
    private Integer employedItemsCount;

    @Column(name = "base_point_in_time")
    private LocalDate basePointInTime;

    @Column(name = "base_index", precision = 15, scale = 2)
    private BigDecimal baseIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Column(name = "favorite", nullable = false)
    private boolean favorite;
}
