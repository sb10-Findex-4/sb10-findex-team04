package com.sprint.mission.findex.indexdata.contoller;

import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.sevice.IndexDataService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<IndexDataDto> createIndexData(@RequestBody IndexDataCreateRequestDto request) {
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
    @DeleteMapping(value = "{id}")
    public void deleteIndexData(@PathVariable Long id) {
        indexDataService.delete(id);
    }

    /*
    TODO:
    지수 데이터 목록 조회
    Get/ /api/index-data
     */
    @GetMapping
    public ResponseEntity<List<IndexDataDto>> getIndexDatas(@RequestBody IndexDataFindListRequestDto request) {
        return ResponseEntity.ok(indexDataService.findAll(request));
    }

    /*
    TODO:
    지수 차트 조회
    Get/ /api/index-data/{id}/chart
     */


    /*
    TODO:
    지수 성과 랭킹 조회
    Get/ /api/index-data/performance/rank
     */


    /*
    TODO:
    관심 지수 성과 조회
    Get/ /api/index-data/performance/facorite
     */


    /*
    TODO:
    지수 데이터 CSV export
    Get/ /api/index-data/export/csv
     */

}
