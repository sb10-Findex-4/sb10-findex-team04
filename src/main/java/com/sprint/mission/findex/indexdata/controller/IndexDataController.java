package com.sprint.mission.findex.indexdata.controller;

import com.sprint.mission.findex.indexdata.dto.response.IndexChartDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.response.CursorPageResponseIndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexPerformanceDto;
import com.sprint.mission.findex.indexdata.dto.response.RankedIndexPerformanceDto;
import com.sprint.mission.findex.indexdata.entity.PeriodType;
import com.sprint.mission.findex.indexdata.service.IndexDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지수 데이터 API", description = "지수 데이터 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;

  /*
  지수 데이터 등록
  Post/ /api/index-data
   */
  @Operation(summary = "지수 데이터 등록", operationId = "create_1", description = "새로운 지수 데이터를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "지수 데이터 생성 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 데이터 값 등)"),
      @ApiResponse(responseCode = "404", description = "참조하는 지수 정보를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping
  public ResponseEntity<IndexDataDto> createIndexData(
      @RequestBody IndexDataCreateRequestDto request) {
    return ResponseEntity.ok(indexDataService.create(request));
  }

  /*
  지수 데이터 수정
  Patch/ /api/index-data/{id}
   */
  @Operation(summary = "지수 데이터 수정", operationId = "update_1", description = "기존 지수 데이터를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "지수 데이터 수정 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 데이터 값 등)"),
      @ApiResponse(responseCode = "404", description = "수정할 지수 데이터를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PatchMapping(value = "/{id}")
  public ResponseEntity<IndexDataDto> updateIndexData(@PathVariable Long id,
      @RequestBody IndexDataUpdateRequestDto request) {
    return ResponseEntity.ok(indexDataService.update(id, request));
  }

  /*
  지수 데이터 삭제
  Delete/ /api/index-data/{id}
   */
  @Operation(summary = "지수 데이터 삭제", operationId = "delete_1", description = "지수 데이터를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "지수 데이터 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "삭제할 지수 데이터를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @DeleteMapping(value = "/{id}")
  public void deleteIndexData(@PathVariable Long id) {
    indexDataService.delete(id);
  }

  /*
  지수 데이터 목록 조회
  Get/ /api/index-data
   */
  @Operation(summary = "지수 데이터 목록 조회", operationId = "findAll_1", description = "지수 데이터 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "지수 데이터 목록 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto<IndexDataDto>> getIndexDatas(
      @ModelAttribute IndexDataFindListRequestDto request) {
    return ResponseEntity.ok(indexDataService.findAll(request));
  }

  /*
  지수 데이터 CSV export
  Get/ /api/index-data/export/csv
   */
  @Operation(summary = "지수 데이터 CSV export", operationId = "exportCSV_1", description = "지수 데이터를 CSV 파일로 export합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "CSV 파일 생성 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping(value = "/export/csv")
  public void exportCsvFile(@ModelAttribute IndexDataExportRequestDto request,
                            HttpServletResponse response) throws Exception {
    response.setContentType("text/csv");
    response.setHeader(
        "Content-Disposition",
        "attachment: filename=index-data.csv"
    );

    indexDataService.exportCsv(request, response.getWriter());
    response.getWriter().flush();
  }

  // 대시보드
    /*
    지수 차트 조회
    Get/ /api/index-data/{id}/chart
     */
  @Operation(summary = "지수 차트 조회", operationId = "findChart_1", description = "지수의 차트 데이터를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "차트 데이터 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
      @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/{id}/chart")
  public ResponseEntity<IndexChartDto> getIndexChart(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "periodType", defaultValue = "1M") String period) {

    IndexChartDto response = indexDataService.getIndexChart(id, period);

    return ResponseEntity.ok(response);
  }

    /*
    지수 성과 랭킹 조회
    Get/ /api/index-data/performance/rank
     */
  @Operation(summary = "지수 성과 랭킹 조회", operationId = "findAllRankings_1", description = "지수의 성과 분석 랭킹을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성과 랭킹 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/performance/rank")
    public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexRankings(
        @RequestParam(name = "periodType", defaultValue = "MONTHLY") PeriodType period,
        @RequestParam(name = "classification", required = false) String classification) {

      List<RankedIndexPerformanceDto> response = indexDataService.getIndexRankings(period, classification);

      return ResponseEntity.ok(response);
    }

  /*
  주요 지수 조회 (관심 지수 성과 조회)
  Get/ /api/index-data/performance/favorite
   */
  @Operation(summary = "관심 지수 성과 조회", operationId = "findAllFavorites_1", description = "즐겨찾기로 등록된 지수들의 성과를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "관심 지수 성과 조회 성공"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceDto>> getFavoriteIndexSummary(
      @RequestParam(name = "periodType", defaultValue = "DAILY") PeriodType period) {

    // 서비스에서 즐겨찾기 필터링이 포함된 로직 호출
    List<IndexPerformanceDto> response = indexDataService.getFavoriteIndexSummary(period);

    return ResponseEntity.ok(response);
  }
}
