package com.sprint.mission.findex.indexinfo.repository;

import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import org.springframework.data.domain.Pageable;
import java.util.List;

/*
 * 지수 정보 필터링,페이지네이션, 중복 검사를 위한 커스텀 Repository 인터페이스
 */
public interface IndexInfoRepositoryCustom {
    // 필터링 + 정렬 + 페이지네이션
    List<IndexInfo> filter(IndexInfoSearchRequestDto request);

    // 조건에 맞는 데이터 개수 반환
    int count(IndexInfoSearchRequestDto request);
}
