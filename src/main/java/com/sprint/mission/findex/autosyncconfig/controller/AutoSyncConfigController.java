// 자동 연동 설정 API 요청(PATCH, GET)을 처리하는 컨트롤러
package com.sprint.mission.findex.autosyncconfig.controller;

import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigSearchRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.request.AutoSyncConfigUpdateRequestDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.AutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.dto.response.CursorPageResponseAutoSyncConfigDto;
import com.sprint.mission.findex.autosyncconfig.service.AutoSyncConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AutoSyncConfig", description = "자동 연동 설정 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auto-sync-configs")
public class AutoSyncConfigController {
  private final AutoSyncConfigService service;

  @Operation(summary = "자동 연동 설정 목록 조회", operationId = "findAll_3")
  @GetMapping
  public ResponseEntity<CursorPageResponseAutoSyncConfigDto> findAll(@RequestBody AutoSyncConfigSearchRequestDto request) {
    CursorPageResponseAutoSyncConfigDto response = service.findAll(request);

    return ResponseEntity.ok(response);
  }

  //활성화 여부 수정
  @Operation(summary = "자동 연동 설정 수정", operationId = "update_3")
  @PatchMapping("/{id}")
  public ResponseEntity<AutoSyncConfigDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody AutoSyncConfigUpdateRequestDto request) {
    AutoSyncConfigDto response = service.update(id, request);

    return ResponseEntity.ok(response);
  }
}
