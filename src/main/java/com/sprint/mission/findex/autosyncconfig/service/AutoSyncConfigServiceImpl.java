package com.sprint.mission.findex.autosyncconfig.service;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigSearchRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import com.sprint.mission.findex.autosyncconfig.exception.AutoSyncConfigNotFoundException;
import com.sprint.mission.findex.autosyncconfig.mapper.AutoSyncConfigMapper;
import com.sprint.mission.findex.autosyncconfig.repository.AutoSyncConfigRepository;
import com.sprint.mission.findex.autosyncconfig.specification.AutoSyncConfigSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AutoSyncConfig 서비스 구현체 설정 조회 / 수정 / 대상 조회 기능 담당
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoSyncConfigServiceImpl implements AutoSyncConfigService {

  private final AutoSyncConfigRepository repository;
  private final AutoSyncConfigMapper mapper;

  /**
   * 자동 연동 설정 목록 조회
   * indexInfoId, enabled 조건으로 필터링
   * 커서 기반 페이지네이션 적용
   * AutoSyncConfig -> DTO 변환
   * 다음 페이지 존재 여부 계산
   */
  @Override
  public CursorPageResponseAutoSyncConfigDto findAll(AutoSyncConfigSearchRequestDto request) {

    // 필터 조건 생성 (indexInfoId + enabled)
    Specification<AutoSyncConfig> specification = Specification
        .where(AutoSyncConfigSpecification.hasIndexInfoId(request.indexInfoId()))
        .and(AutoSyncConfigSpecification.hasEnabled(request.enabled()));

    // 커서가 존재할 경우 페이지네이션 조건 추가
    if (request.cursor() != null) {
      Long cursorId = Long.valueOf(request.cursor());

      if ("ASC".equalsIgnoreCase(request.sortDirection())) {
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("id"), cursorId));
      } else {
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("id"), cursorId));
      }
    }

    // 전체 데이터 개수 조회
    long totalElements = repository.count(specification);

// 정렬 적용
    Sort.Direction direction = "ASC".equalsIgnoreCase(request.sortDirection())
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;

// 다음 페이지 존재 여부 확인을 위해 size + 1 조회
    Pageable pageable = PageRequest.of(
        0,
            request.size() + 1,
        Sort.by(direction, "id")
    );

    List<AutoSyncConfig> entities = repository.findAll(specification, pageable).getContent();

    // 다음 페이지 존재 여부 판단
    boolean hasNext = entities.size() > request.size();

    // size 초과 시 마지막 요소 제거
    if (hasNext) {
      entities = entities.subList(0, request.size());
    }

    // Entity -> DTO 변환
    List<AutoSyncConfigDto> content = entities.stream()
        .map(mapper::toDto)
        .toList();

    // 다음 페이지 기준 ID 계산
    Long nextIdAfter = hasNext
        ? entities.get(entities.size() - 1).getId()
        : null;

    // nextCursor 생성
    String nextCursor = nextIdAfter != null
        ? String.valueOf(nextIdAfter)
        : null;

    // 커서 페이지 응답 DTO 반환
    return new CursorPageResponseAutoSyncConfigDto(
        content,
        nextCursor,
        nextIdAfter,
        request.size(),
        totalElements,
        hasNext
    );
  }

  /**
   * 자동 연동 활성화 상태 수정 AutoSyncConfig의 enabled 값을 수정한다.
   */
  @Override
  @Transactional
  public AutoSyncConfigDto update(Long id, AutoSyncConfigUpdateRequestDto request) {

    // AutoSyncConfig 조회
    AutoSyncConfig entity = repository.findById(id)
        .orElseThrow(AutoSyncConfigNotFoundException::new);

    // enabled 상태 수정
    entity.update(request.enabled());

    // DTO 반환
    return mapper.toDto(entity);
  }

  /**
   * 자동 연동 대상 조회
   * enabled=true 상태인 AutoSyncConfig만 조회한다.
   * Scheduler에서 자동 연동 대상 목록을 가져올 때 사용한다.
   */
  @Override
  public List<AutoSyncConfig> findAllEnabledAutoSyncConfigs() {
    return repository.findAllByEnabledTrue();
  }
}
