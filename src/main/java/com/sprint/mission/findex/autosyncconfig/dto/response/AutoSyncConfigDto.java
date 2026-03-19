package com.sprint.mission.findex.autosyncconfig.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// 조회 응답 DTO
@Schema(description = "자동 연동 설정 DTO")
public record AutoSyncConfigDto (
        @Schema(description = "자동 연동 설정 ID", example = "1")
        Long id,

        @Schema(description = "지수 정보 ID", example = "1")
        Long indexInfoId,

        @Schema(description = "지수 분류명", example = "KOSPI시리즈")
        String indexClassification,

        @Schema(description = "지수명", example = "IT 서비스")
        String indexName,

        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) {
}
