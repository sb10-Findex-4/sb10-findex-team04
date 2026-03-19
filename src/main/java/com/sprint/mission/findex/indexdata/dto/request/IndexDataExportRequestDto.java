package com.sprint.mission.findex.indexdata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "지수 데이터 CSV export 요청 DTO")
public record IndexDataExportRequestDto(
    // 검색 필터 조건
    @Schema(description = "대상 지수 정보 ID (비어있을 경우 전체 지수 대상)", example = "1")
    Long indexInfoId,

    @Schema(description = "시작 일자", example = "2026-03-01")
    LocalDate startDate,

    @Schema(description = "종료 일자", example = "2026-03-19")
    LocalDate endDate,

    // 정렬 조건
    @Schema(
        description = "정렬 필드", example = "baseDate", defaultValue = "baseDate")
    String sortField,           // 기본값: baseDate

    @Schema(description = "정렬 방향", example = "desc", allowableValues = {"asc", "desc"}, defaultValue = "desc")
    String sortDirection        // 기본값: desc
) {

  public IndexDataExportRequestDto {
    if (sortDirection == null) {
      sortDirection = "desc";
    }

    if (sortField == null) {
      sortField = "baseDate";
    }
  }
}
