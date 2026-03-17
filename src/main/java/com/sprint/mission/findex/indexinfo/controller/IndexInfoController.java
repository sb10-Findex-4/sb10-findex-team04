package com.sprint.mission.findex.indexinfo.controller;

import com.sprint.mission.findex.indexdata.dto.response.IndexChartDto;
import com.sprint.mission.findex.indexdata.dto.response.RankedIndexPerformanceDto;
import com.sprint.mission.findex.indexdata.service.IndexDataService;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.CursorPageResponseIndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoSummaryDto;
import com.sprint.mission.findex.indexinfo.service.IndexInfoService;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController {
    private final IndexInfoService indexInfoService;
    private final IndexDataService indexDataService;

    /*
    지수 정보 등록 (사용자 수동)
    Post/ /api/index-infos
     */
    @PostMapping
    public ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody IndexInfoCreateRequestDto request){
        return ResponseEntity.ok(indexInfoService.createIndexInfo(request));
    }

    /*
    지수 정보 조회
    Get/ /api/index-infos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexInfoDto> getIndexInfo(@PathVariable Long id){
        return ResponseEntity.ok(indexInfoService.getIndexInfoById(id));
    }

    /*
    지수 정보 삭제
    Delete/ /api/index-infos/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        indexInfoService.deleteIndexInfoById(id);
    }

    /*
    지수 정보 수정 (사용자 수동)
    Patch/ /api/index-infos/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoUpdateRequestDto request){
        return ResponseEntity.ok(indexInfoService.updateIndexInfoById(id, request));
    }

    /*
    지수 정보 요약 목록 조회
    Get/ /api/index-infos/summaries
     */
    @GetMapping("/summaries")
    public ResponseEntity<List<IndexInfoSummaryDto>> getIndexInfoSummaries(){
        return ResponseEntity.ok(indexInfoService.findAllSummaries());
    }

    /*
    지수 정보 목록 조회
    Get/ /api/index-infos
     */
    @GetMapping
    public ResponseEntity<CursorPageResponseIndexInfoDto<IndexInfoDto>> getIndexInfos(IndexInfoSearchRequestDto request){
        CursorPageResponseIndexInfoDto<IndexInfoDto> page = indexInfoService.findAll(request);
        return ResponseEntity.ok(page);
    }

    /*
     [대시보드] 지수 차트 데이터 조회
     */
    @GetMapping("/charts/{id}")
    public ResponseEntity<IndexChartDto> getIndexChart(
        @PathVariable(name = "id") Long id,
        @RequestParam(name = "period", defaultValue = "1M") String period) {

        IndexChartDto response = indexDataService.getIndexChart(id, period);

        return ResponseEntity.ok(response);
    }

    /*
    [대시보드] 지수 성과 랭킹 조회
    */
    @GetMapping("/rankings")
    public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexRankings(
        @RequestParam(name = "period", defaultValue = "1M") String period,
        @RequestParam(name = "classification", required = false) String classification) {

        List<RankedIndexPerformanceDto> response = indexDataService.getIndexRankings(period, classification);

        return ResponseEntity.ok(response);
    }
}
