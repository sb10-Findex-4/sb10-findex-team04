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
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;

  /*
  지수 데이터 등록
  Post/ /api/index-data
   */
  @PostMapping
  public ResponseEntity<IndexDataDto> createIndexData(
      @RequestBody IndexDataCreateRequestDto request) {
    return ResponseEntity.ok(indexDataService.create(request));
  }

  /*
  지수 데이터 수정
  Patch/ /api/index-data/{id}
   */
  @PatchMapping(value = "/{id}")
  public ResponseEntity<IndexDataDto> updateIndexData(@PathVariable Long id,
      @RequestBody IndexDataUpdateRequestDto request) {
    return ResponseEntity.ok(indexDataService.update(id, request));
  }

  /*
  지수 데이터 삭제
  Delete/ /api/index-data/{id}
   */
  @DeleteMapping(value = "/{id}")
  public void deleteIndexData(@PathVariable Long id) {
    indexDataService.delete(id);
  }

  /*
  TODO:
  지수 데이터 목록 조회
  Get/ /api/index-data
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto<IndexDataDto>> getIndexDatas(
      @ModelAttribute IndexDataFindListRequestDto request) {
    return ResponseEntity.ok(indexDataService.findAll(request));
  }

  /*
  TODO:
  지수 데이터 CSV export
  Get/ /api/index-data/export/csv
   */
  @GetMapping(value = "/export/csv")
  public void exportCsvFile(@ModelAttribute IndexDataExportRequestDto request,
                            HttpServletResponse response) throws Exception {
    response.setContentType("text/csv; charset=UTF-8");
    response.setHeader(
        "Content-Disposition",
        "attachment; filename=index-data.csv"
    );

    indexDataService.exportCsv(request, response.getWriter());
    response.getWriter().flush();
  }

  // 대시보드
    /*
    지수 차트 조회
    Get/ /api/index-data/{id}/chart
     */
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
  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceDto>> getFavoriteIndexSummary(
      @RequestParam(name = "periodType", defaultValue = "DAILY") PeriodType period) {

    // 서비스에서 즐겨찾기 필터링이 포함된 로직 호출
    List<IndexPerformanceDto> response = indexDataService.getFavoriteIndexSummary(period);

    return ResponseEntity.ok(response);
  }
}
