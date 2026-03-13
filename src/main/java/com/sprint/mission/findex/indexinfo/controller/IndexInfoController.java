package com.sprint.mission.findex.indexinfo.controller;

import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.CursorPageResponseIndexInfoDto;
import com.sprint.mission.findex.indexinfo.service.IndexInfoService;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController {
    private final IndexInfoService indexInfoService;

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
    @PatchMapping("{id}")
    public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoUpdateRequestDto request){
        return ResponseEntity.ok(indexInfoService.updateIndexInfoById(id, request));
    }

    /*
    TODO:
    지수 정보 요약 목록 조회
    Get/ /api/index-infos/summaries
     */
    @GetMapping("/summaries")
    public ResponseEntity<IndexInfoDto> getIndexInfoSummaries(){
        return null;
    }

    /*
    Todo: QueryDSL로 쿼리 고도화를 통해 정렬까지 구현해야 함 (현재: 필터링 후 Id 기반 페이지네이션만 구현되어 있음)
    지수 정보 목록 조회
    Get/ /api/index-infos
     */
    @GetMapping
    public ResponseEntity<CursorPageResponseIndexInfoDto<IndexInfoDto>> getIndexInfos(IndexInfoSearchRequestDto request){
        CursorPageResponseIndexInfoDto<IndexInfoDto> page = indexInfoService.findAll(request);
        return ResponseEntity.ok(page);
    }
}
