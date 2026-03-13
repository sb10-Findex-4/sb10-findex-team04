package com.sprint.mission.findex.indexdata.repository;

import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
    IndexData save(IndexData indexData);
    Optional<IndexData> findById(Long id);
    List<IndexData> findAll();
    Boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate baseDate);
    void deleteById(Long id);

    // 지수 정보 ID는 완전일치, 날짜는 범위 조건으로 조회
    List<IndexData> findByIndexInfoIdAndBaseDateBetween(Long indexInfoId, LocalDate startDate, LocalDate endDate);
}
