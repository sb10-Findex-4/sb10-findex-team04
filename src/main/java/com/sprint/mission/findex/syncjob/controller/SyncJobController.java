package com.sprint.mission.findex.syncjob.controller;

import com.sprint.mission.findex.syncjob.dto.request.SyncJobCreateRequestDto;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobIndexDataRequestDto;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.service.SyncJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "연동 작업 API", description = "연동 작업 관리 API")
@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {
    private final SyncJobService syncJobService;

    /*
    연동 작업 생성
    ---------------
    Swagger API 명세서에 정의된 파라미터 개수가 많아 SyncJobCreateConditionDto를 RequestBody로 사용함
*/
    @Operation(
        summary = "연동 작업 생성",
        operationId = "create_2",
        description = "연동 작업 결과를 생성합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<List<SyncJobDto>> createSyncJob(
        @RequestBody SyncJobCreateRequestDto syncJobCreateRequestDto,
        HttpServletRequest servletRequest) {
        // 1. 요청 정보에서 클라이언트 IP 추출 (사용자 식별용)
        String clientIp = servletRequest.getRemoteAddr();

        // 2. 생성된 리스트를 body애 담아 상태 코드와 함께 반환
        List<SyncJobDto> results = syncJobService.createSyncJob(syncJobCreateRequestDto, clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    /*
        외부 API 연동 (지수 정보)
     */
    @Operation(
        summary = "지수 정보 연동",
        operationId = "syncIndexInfos_2",
        description = "Open API를 통해 지수 정보를 연동합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/index-infos")
    public ResponseEntity<List<SyncJobDto>> syncIndexInfos(HttpServletRequest request) {
        // Ip 주소 get
        String worker = request.getRemoteAddr();
        return ResponseEntity.ok(syncJobService.syncIndexInfos(worker));
    }

    /*
        외부 API 연동 (지수 데이터)
     */
    @Operation(
        summary = "지수 데이터 연동",
        operationId = "syncIndexData_2",
        description = "Open API를 통해 지수 데이터를 연동합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 날짜 범위 등)"),
        @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/index-data")
    public ResponseEntity<List<SyncJobDto>> syncIndexDatas(@RequestBody SyncJobIndexDataRequestDto requestDto,
                                                          HttpServletRequest request) {
        String worker = request.getRemoteAddr();
        return ResponseEntity.ok(syncJobService.syncIndexData(worker, requestDto.indexInfoIds(), requestDto.baseDateFrom(), requestDto.baseDateTo()));
    }

    /*
        연동 작업 목록 조회
        ---------------
        Swagger API 명세서에 정의된 파라미터 개수가 많아 하나의 Dto로 묶어서 처리
        별도로 default 값이 명시된 필드만 컨트롤러의 파라미터로 주입
    */
    @Operation(
        summary = "연동 작업 목록 조회",
        operationId = "findAll_2",
        description = "연동 작업 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "연동 작업 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto<SyncJobDto>> findAllSyncJobs(@ModelAttribute SyncJobSearchConditionDto syncJobSearchConditionDto) {
        CursorPageResponseSyncJobDto<SyncJobDto> response = syncJobService.findAllSyncJobs(syncJobSearchConditionDto);

        return ResponseEntity.ok(response);
    }
}
