package com.sprint.mission.findex.autosyncconfig.service;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoSyncConfigServiceImpl implements AutoSyncConfigService {

  private final AutoSyncConfigRepository repository;
  private final AutoSyncConfigMapper mapper;

  @Override
  public CursorPageResponseAutoSyncConfigDto findAll(
      Long indexInfoId,
      Boolean enabled,
      String cursor,
      String sortField,
      String sortDirection,
      int size
  ) {

    Specification<AutoSyncConfig> specification = Specification
        .where(AutoSyncConfigSpecification.hasIndexInfoId(indexInfoId))
        .and(AutoSyncConfigSpecification.hasEnabled(enabled));

    // 페이지네이션
    if (cursor != null) {
      Long cursorId = Long.valueOf(cursor);

      if ("ASC".equalsIgnoreCase(sortDirection)) {
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("id"), cursorId));
      } else {
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("id"), cursorId));
      }
    }

    // 전체 개수 조회
    long totalElements = repository.count(specification);

    // 다음 페이지 존재 여부 확인을 위해 size + 1 조회
    Pageable pageable = PageRequest.of(0, size + 1);
    List<AutoSyncConfig> entities = repository.findAll(specification, pageable).getContent();

    // 다음 페이지 존재 여부 판단
    boolean hasNext = entities.size() > size;

    // size 초과 시 마지막 1개 제거
    if (hasNext) {
      entities = entities.subList(0, size);
    }

    List<AutoSyncConfigDto> content = entities.stream()
        .map(mapper::toDto)
        .toList();

    // 다음 페이지 기준 id
    Long nextIdAfter = hasNext
        ? entities.get(entities.size() - 1).getId()
        : null;

    // nextCursor는 문자열이라 nextIdAfter를 문자열로 변환해서 사용
    String nextCursor = nextIdAfter != null
        ? String.valueOf(nextIdAfter)
        : null;

    return new CursorPageResponseAutoSyncConfigDto(
        content,
        nextCursor,
        nextIdAfter,
        content.size(),
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional
  public AutoSyncConfigDto update(Long id, AutoSyncConfigUpdateRequestDto request) {
    AutoSyncConfig entity = repository.findById(id)
        .orElseThrow(AutoSyncConfigNotFoundException::new);

    entity.update(request.enabled());

    return mapper.toDto(entity);
  }

  @Override
  public List<AutoSyncConfig> findAllEnabledAutoSyncConfigs() {
    return repository.findAllByEnabledTrue();
  }
}