package com.sprint.mission.findex.syncJob.controller;

import com.sprint.mission.findex.syncJob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncJob.entity.SyncJob;
import com.sprint.mission.findex.syncJob.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SyncJob", description = "SyncJob API")
@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {
    private final SyncService syncService;

    // 연동 작업 목록 조회
    @Operation(summary = "연동 작업 목록 조회", operationId = "getSyncJobs")
    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto<SyncJob>> findAllSyncJobs(
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) String baseDateFrom,
            @RequestParam(required = false) String baseDateTo,
            @RequestParam(required = false) String worker,
            @RequestParam(required = false) String jobTimeFrom,
            @RequestParam(required = false) String jobTimeTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "10") int size
    ) {
        CursorPageResponseSyncJobDto<SyncJob> response = syncService.findAllSyncJobs();

        return ResponseEntity.ok(response);
    }
}
