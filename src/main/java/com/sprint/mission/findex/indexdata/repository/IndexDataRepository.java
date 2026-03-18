package com.sprint.mission.findex.indexdata.repository;

import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
    // 중복 검사
    Boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate baseDate);

    // 지수 정보 ID는 완전일치, 날짜는 범위 조건으로 조회
    List<IndexData> findByIndexInfoIdAndBaseDateBetween(Long indexInfoId, LocalDate startDate, LocalDate endDate);

    // 정렬 포함
    List<IndexData> findByIndexInfoIdAndBaseDateBetween(Long indexInfoId, LocalDate startDate, LocalDate endDate, Sort sort);

    IndexData findByIndexInfoIdAndBaseDate(Long infoId, LocalDate baseDate);

    // 커서가 없는 지수 데이터 목록 조회
    // 테스트 에러 임시 해결:
    @Query("SELECT i FROM IndexData i")
    List<IndexData> findFirstPageIndexDatas(IndexDataFindListRequestDto request, Pageable pageable);

    // 커서가 있는 지수 데이터 목록 조회
    // 테스트 에러 임시 해결:
    @Query("SELECT i FROM IndexData i WHERE i.id > :idAfter")
    List<IndexData> findNextPageIndexDatasById(IndexDataFindListRequestDto request, Long idAfter, Pageable pageable);
}
