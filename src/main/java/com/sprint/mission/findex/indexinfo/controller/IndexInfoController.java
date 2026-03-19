package com.sprint.mission.findex.indexinfo.controller;

import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.CursorPageResponseIndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoSummaryDto;
import com.sprint.mission.findex.indexinfo.service.IndexInfoService;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "지수 정보 API", description = "지수 정보 관리 API")
@RestController
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController {
    private final IndexInfoService indexInfoService;

    /*
    지수 정보 등록 (사용자 수동)
    Post/ /api/index-infos
     */
    @Operation(summary = "지수 정보 등록", operationId = "create", description = "새로운 지수 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "지수 정보 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락 등)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody IndexInfoCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(indexInfoService.createIndexInfo(request));
    }

    /*
    지수 정보 조회
    Get/ /api/index-infos/{id}
     */
    @Operation(summary = "지수 정보 조회", operationId = "find", description = "ID로 지수 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지수 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회할 지수 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<IndexInfoDto> getIndexInfo(@PathVariable Long id){
        return ResponseEntity.ok(indexInfoService.getIndexInfoById(id));
    }

    /*
    지수 정보 삭제
    Delete/ /api/index-infos/{id}
     */
    @Operation(summary = "지수 정보 삭제", operationId = "delete", description = "지수 정보를 삭제합니다. 관련된 지수 데이터도 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "지수 정보 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "삭제할 지수 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        indexInfoService.deleteIndexInfoById(id);

        return ResponseEntity.noContent().build();
    }

    /*
    지수 정보 수정 (사용자 수동)
    Patch/ /api/index-infos/{id}
     */
    @Operation(summary = "지수 정보 수정", operationId = "update", description = "기존 지수 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지수 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필드 값 등)"),
            @ApiResponse(responseCode = "404", description = "수정할 지수 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoUpdateRequestDto request){
        return ResponseEntity.ok(indexInfoService.updateIndexInfoById(id, request));
    }

    /*
    지수 정보 요약 목록 조회
    Get/ /api/index-infos/summaries
     */
    @Operation(summary = "지수 정보 요약 목록 조회", operationId = "findAllSummaries", description = "지수 ID, 분류, 이름만 포함한 전체 지수 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지수 정보 요약 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/summaries")
    public ResponseEntity<List<IndexInfoSummaryDto>> getIndexInfoSummaries(){
        return ResponseEntity.ok(indexInfoService.findAllSummaries());
    }

    /*
    지수 정보 목록 조회
    Get/ /api/index-infos
     */
    @Operation(summary = "지수 정보 목록 조회", operationId = "findAll", description = "지수 정보 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지수 정보 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<CursorPageResponseIndexInfoDto<IndexInfoDto>> getIndexInfos(IndexInfoSearchRequestDto request){
        CursorPageResponseIndexInfoDto<IndexInfoDto> page = indexInfoService.findAll(request);
        return ResponseEntity.ok(page);
    }
}
