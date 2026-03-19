package com.sprint.mission.findex.indexdata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "지수 데이터 CSV export 요청 DTO")
public record IndexDataExportRequestDto(
    // 검색 필터 조건
    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,

    @Schema(description = "시작 일자", example = "2026-03-01")
    LocalDate startDate,

    @Schema(description = "종료 일자", example = "2026-03-19")
    LocalDate endDate,

    // 정렬 조건
    @Schema(
        description = "정렬 필드 (baseDate, marketPrice, closingPrice, highPrice, lowPrice, versus, fluctuationRate, tradingQuantity, tradingPrice, marketTotalAmount)",
        example = "baseDate", defaultValue = "baseDate")
    String sortField,           // 기본값: baseDate

    @Schema(description = "정렬 방향 (asc, desc)", example = "desc", allowableValues = {"asc", "desc"}, defaultValue = "desc")
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
