package com.sprint.mission.findex.indexdata.repository;

import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long>, IndexDataRepositoryCustom {
    // 중복 검사
    Boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate baseDate);

    // 지수 정보 ID는 완전일치, 날짜는 범위 조건으로 조회
    List<IndexData> findByIndexInfoIdAndBaseDateBetween(Long indexInfoId, LocalDate startDate, LocalDate endDate);

    // 정렬 포함
    List<IndexData> findByIndexInfoIdAndBaseDateBetween(Long indexInfoId, LocalDate startDate, LocalDate endDate, Sort sort);

    // 대시보드 차트용: 여러 지수 ID 리스트와 특정 날짜에 해당하는 시세 데이터를 한 번에 조회
    List<IndexData> findByIndexInfoIdInAndBaseDate(List<Long> indexInfoIds, LocalDate baseDate);

    // 대시보드 차트용: 특정 지수의 데이터를 날짜 오름차순으로 조회
    List<IndexData> findByIndexInfoIdAndBaseDateBetweenOrderByBaseDateAsc(Long indexInfoId, LocalDate startDate, LocalDate endDate);

    // 대시보드 차트용: 특정 날짜에 해당하는 모든 지수의 성과 데이터 조회
    List<IndexData> findByBaseDate(LocalDate baseDate);

    void deleteIndexDataByIndexInfoId(Long indexInfoId);
}
