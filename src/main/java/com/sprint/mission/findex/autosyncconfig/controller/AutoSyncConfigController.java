// 자동 연동 설정 API 요청(PATCH, GET)을 처리하는 컨트롤러
package com.sprint.mission.findex.autosyncconfig.controller;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigSearchRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.service.AutoSyncConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auto-sync-configs")
public class AutoSyncConfigController {
  private final AutoSyncConfigService service;

  @GetMapping
  public CursorPageResponseAutoSyncConfigDto findAll(
      AutoSyncConfigSearchRequestDto request
  ) {
    return service.findAll(
        request.indexInfoId(),
        request.enabled(),
        request.cursor(),
        request.sortField(),
        request.sortDirection(),
        request.size()
    );
  }

  //활성화 여부 수정
  @PatchMapping("/{id}")
  public AutoSyncConfigDto update(
      @PathVariable Long id,
      @Valid
      @RequestBody AutoSyncConfigUpdateRequestDto request
  ) {
    return service.update(id, request);
  }
}
