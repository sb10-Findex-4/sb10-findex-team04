package com.sprint.mission.findex.autosyncconfig.dto.request;

import jakarta.validation.constraints.NotNull;

// 자동 연동 목록 활성화 변경 DTO
public record AutoSyncConfigUpdateRequestDto(
    @NotNull
    Boolean enabled
) {
}
