package com.sprint.mission.findex.indexdata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;

@Builder
@Schema(description = "커서 기반 페이징 응답 DTO")
public record CursorPageResponseIndexDataDto<T>(
    @Schema(description = "페이지 내용", implementation = IndexDataDto.class)
    List<IndexDataDto> content,

    @Schema(description = "다음 페이지 커서", example = "eyJpZCI6MjB9")
    String nextCursor,

    @Schema(description = "마지막 요소의 ID", example = "eyJpZCI6MjB9")
    Long nextIdAfter,

    @Schema(description = "페이지 크기", example = "10")
    int size,

    @Schema(description = "조건에 부합하는 총 요소 개수", example = "150")
    int totalElements,

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    Boolean hasNext
) {

}
