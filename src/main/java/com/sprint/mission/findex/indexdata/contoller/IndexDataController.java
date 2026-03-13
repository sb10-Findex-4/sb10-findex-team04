package com.sprint.mission.findex.indexdata.contoller;

import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.sevice.IndexDataService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {
    private final IndexDataService indexDataService;

    @PostMapping
    public ResponseEntity<IndexDataDto> createIndexData(@RequestBody IndexDataCreateRequestDto request) {
        return ResponseEntity.ok(indexDataService.create(request));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<IndexDataDto> updateIndexData(@PathVariable Long id,
                                                        @RequestBody IndexDataUpdateRequestDto request) {
        return ResponseEntity.ok(indexDataService.update(id, request));
    }

    @DeleteMapping(value = "{id}")
    public void deleteIndexData(@PathVariable Long id) {
        indexDataService.delete(id);
    }

}
