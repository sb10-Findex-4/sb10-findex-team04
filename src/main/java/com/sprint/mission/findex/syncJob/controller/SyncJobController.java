package com.sprint.mission.findex.syncJob.controller;

import com.sprint.mission.findex.syncJob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncJob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncJob.entity.SyncJob;
import com.sprint.mission.findex.syncJob.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "SyncJob", description = "SyncJob API")
@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {
    private final SyncService syncService;

    /*
        연동 작업 목록 조회
        ---------------
        Swagger APU 명세서에 정의된 파라미터 개수가 많아 하나의 Dto로 묶어서 처리
        별도로 default 값이 명시된 필드만 컨트롤러의 파라미터로 주입
    */
    @Operation(summary = "연동 작업 목록 조회", operationId = "getSyncJobs")
    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto<SyncJob>> findAllSyncJobs(@ModelAttribute SyncJobSearchConditionDto syncJobSearchConditionDto) {
        CursorPageResponseSyncJobDto<SyncJob> response = syncService.findAllSyncJobs(syncJobSearchConditionDto);

        return ResponseEntity.ok(response);
    }
}
