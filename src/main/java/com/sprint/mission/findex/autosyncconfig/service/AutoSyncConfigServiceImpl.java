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
      Long cursor,
      int pageSize
  ) {

    Specification<AutoSyncConfig> specification = Specification
        .where(AutoSyncConfigSpecification.hasIndexInfoId(indexInfoId))
        .and(AutoSyncConfigSpecification.hasEnabled(enabled));

    // 커서가 있으면 해당 id보다 작은 데이터만 조회
    if (cursor != null) {
      specification = specification.and((root, query, criteriaBuilder) ->
          criteriaBuilder.lessThan(root.get("id"), cursor));
    }

    // 전체 개수 조회
    long totalElements = repository.count(specification);

    // 다음 페이지 존재 여부 확인을 위해 pageSize + 1 조회
    Pageable pageable = PageRequest.of(0, pageSize + 1);
    List<AutoSyncConfig> entities = repository.findAll(specification, pageable).getContent();

    // 다음 페이지 존재 여부 판단
    boolean hasNext = entities.size() > pageSize;

    // pageSize 초과 시 마지막 1개 제거
    if (hasNext) {
      entities = entities.subList(0, pageSize);
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