// 자동 연동 설정 API 요청(PATCH, GET)을 처리하는 컨트롤러
package com.sprint.mission.findex.autosyncconfig.controller;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.entity.AutoSyncConfig;
import com.sprint.mission.findex.autosyncconfig.exception.AutoSyncConfigNotFoundException;
import com.sprint.mission.findex.autosyncconfig.repository.AutoSyncConfigRepository;
import com.sprint.mission.findex.autosyncconfig.service.AutoSyncConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auto-sync-configs")
public class AutoSyncConfigController {

  private final AutoSyncConfigService service;

  // 테스트를 위해 추가 확인 완료 후 삭제
  private final AutoSyncConfigRepository repository;

  @GetMapping
  public CursorPageResponseAutoSyncConfigDto findAll(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) Boolean enabled,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int size
  ) {
    return service.findAll(indexInfoId, enabled, cursor, sortField, sortDirection, size);
  }

  //활성화 여부 수정
  @PatchMapping("/{id}")
  public AutoSyncConfigDto update(
      @PathVariable Long id,
      @RequestBody AutoSyncConfigUpdateRequestDto request
  ) {
    return service.update(id, request);
  }

  /*
   테스트용 코드 (PATCH 활성화 수정 테스트)
   테스트 후 삭제 예정
   테스트 데이터 부재로 인해 실데이터 토글 테스트는 미확인.
   존재하지 않는 id 요청 시 404 예외 응답은 정상 확인.
   http://localhost:8080/api/auto-sync-configs/test-enable
   */
  @GetMapping("/test-enable")
  public String testEnable() {

    AutoSyncConfig entity = repository.findById(1L)
        .orElseThrow(AutoSyncConfigNotFoundException::new);

    boolean before = entity.isEnabled();

    entity.update(!before);

    return "enabled 변경: " + before + " -> " + entity.isEnabled();
  }
}
