package com.sprint.mission.findex.autosyncconfig.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// 자동 연동 목록 활성화 변경 DTO
@Schema(description = "자동 연동 설정 수정 요청")
public record AutoSyncConfigUpdateRequestDto(
        @Schema(description = "활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Boolean enabled
) {
}
