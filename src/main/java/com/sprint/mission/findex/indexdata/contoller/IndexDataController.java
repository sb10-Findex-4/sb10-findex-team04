package com.sprint.mission.findex.indexdata.contoller;

import com.sprint.mission.findex.indexdata.dto.response.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.response.CursorPageResponseIndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.entity.SourceType;
import com.sprint.mission.findex.indexdata.service.IndexDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<IndexDataDto> createIndexData(@RequestBody IndexDataCreateRequestDto request,
                                                        HttpServletRequest httpServletRequest) {

        // 외부 API에서 등록하는 건지 확인
        SourceType sourceType = "OPEN_API".equals(httpServletRequest.getHeader("X-Source-Type"))
                ? SourceType.OPEN_API
                : SourceType.USER;

        return ResponseEntity.ok(indexDataService.create(request, sourceType));
    }

    /*
    지수 데이터 수정
    Patch/ /api/index-data/{id}
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<IndexDataDto> updateIndexData(@PathVariable Long id,
                                                        @RequestBody IndexDataUpdateRequestDto request,
                                                        HttpServletRequest httpServletRequest) {

        // 외부 API에서 수정하는 건지 확인
        SourceType sourceType = "OPEN_API".equals(httpServletRequest.getHeader("X-Source-Type"))
                ? SourceType.OPEN_API
                : SourceType.USER;

        return ResponseEntity.ok(indexDataService.update(id, request, sourceType));
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
    public ResponseEntity<CursorPageResponseIndexDataDto<IndexDataDto>> getIndexDatas(@ModelAttribute IndexDataFindListRequestDto request) {
        return ResponseEntity.ok(indexDataService.findAll(request));
    }

    /*
    TODO:
    지수 데이터 CSV export
    Get/ /api/index-data/export/csv
     */
    @GetMapping(value = "/export/csv")
    public ResponseEntity<String> exportCsvFile(@ModelAttribute IndexDataExportRequestDto request,
                                                HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment: filename=index-data.csv"
        );

        indexDataService.exportCsv(request, response.getWriter());
        return ResponseEntity.ok("지수 데이터 CSV파일 Export");
    }

    // 여기 부분은 대시보드 작업 api가 Swagger에 있었습니다.
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

}
