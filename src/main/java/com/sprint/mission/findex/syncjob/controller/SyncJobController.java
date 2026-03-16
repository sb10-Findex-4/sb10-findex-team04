package com.sprint.mission.findex.syncjob.controller;

import com.sprint.mission.findex.syncjob.dto.request.SyncJobCreateRequestDto;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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
    연동 작업 생성
    ---------------
    Swagger API 명세서에 정의된 파라미터 개수가 많아 SyncJobCreateConditionDto를 RequestBody로 사용함
*/
    @Operation(summary = "연동 작업 생성", operationId = "createSyncJobs")
    @PostMapping
    public ResponseEntity<List<SyncJobDto>> createSyncJob(
        @RequestBody SyncJobCreateRequestDto syncJobCreateRequestDto,
        HttpServletRequest servletRequest) {
        // 1. 요청 정보에서 클라이언트 IP 추출 (사용자 식별용)
        String clientIp = servletRequest.getRemoteAddr();

        // 2. 서비스 호출 및 결과 데이터 수집
        syncService.createSyncJob(syncJobCreateRequestDto, clientIp);

        // 3. 생성된 리스트를 body애 담아 상태 코드와 함께 반환
        return ResponseEntity.ok().build();
    }

    /*
        연동 작업 목록 조회
        ---------------
        Swagger API 명세서에 정의된 파라미터 개수가 많아 하나의 Dto로 묶어서 처리
        별도로 default 값이 명시된 필드만 컨트롤러의 파라미터로 주입
    */
    @Operation(summary = "연동 작업 목록 조회", operationId = "getSyncJobs")
    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto<SyncJobDto>> findAllSyncJobs(@ModelAttribute SyncJobSearchConditionDto syncJobSearchConditionDto) {
        CursorPageResponseSyncJobDto<SyncJobDto> response = syncService.findAllSyncJobs(syncJobSearchConditionDto);

        return ResponseEntity.ok(response);
    }
}
