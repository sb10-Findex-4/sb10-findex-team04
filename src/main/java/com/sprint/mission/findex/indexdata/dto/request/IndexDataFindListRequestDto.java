package com.sprint.mission.findex.indexdata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "지수 데이터 목록 조회 필터 및 페이징 DTO")
public record IndexDataFindListRequestDto(
    // 검색 필터 조건
    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,

    @Schema(description = "시작 일자", example = "2026-03-01")
    LocalDate startDate,

    @Schema(description = "종료 일자", example = "2026-03-19")
    LocalDate endDate,

    // 페이징 및 정렬
    @Schema(description = "이전 페이지 마지막 요소 ID", example = "100")
    Long idAfter,

    @Schema(description = "커서 (다음 페이지 시작점)", example = "Y3Vyc29y")
    String cursor,

    @Schema(description = "정렬 필드", example = "baseDate", defaultValue = "baseDate")
    String sortField,           // 기본값: baseDate

    @Schema(description = "정렬 방향", example = "desc", allowableValues = {"asc", "desc"}, defaultValue = "desc")
    String sortDirection,       // 기본값: desc

    @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
    Integer size                // 기본값: 10
) {

  public IndexDataFindListRequestDto {
    if (size == null) {
      size = 10;
    }

    if (sortDirection == null) {
      sortDirection = "desc";
    }

    if (sortField == null) {
      sortField = "baseDate";
    }
  }
}
