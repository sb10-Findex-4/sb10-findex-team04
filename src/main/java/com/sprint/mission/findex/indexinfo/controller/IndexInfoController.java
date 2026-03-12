package com.sprint.mission.findex.indexinfo.controller;

import com.sprint.mission.findex.indexinfo.service.IndexInfoService;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequest;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequest;
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
    public ResponseEntity<IndexInfoDto> create(@RequestBody IndexInfoCreateRequest request){
        return ResponseEntity.ok(indexInfoService.createIndexInfo(request));
    }

    /*
    지수 정보 조회
    Get/ /api/index-infos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexInfoDto> get(@PathVariable Long id){
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
    지수 정보 수정
     */
    @PatchMapping("{id}")
    public ResponseEntity<IndexInfoDto> update(@PathVariable Long id, @RequestBody IndexInfoUpdateRequest request){
        return null;
    }

    /*
    지수 정보 요약 목록 조회
     */
    @GetMapping("/summaries")
    public ResponseEntity<IndexInfoDto> list(){
        return null;
    }

}
