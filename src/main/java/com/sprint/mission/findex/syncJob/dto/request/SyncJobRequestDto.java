package com.sprint.mission.findex.syncJob.dto.request;

import com.sprint.mission.findex.syncJob.entity.JobResult;
import com.sprint.mission.findex.syncJob.entity.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@Schema(description = "연동 이력 생성 요청 DTO")
public record SyncJobRequestDto (

    @Schema(description = "연동 작업 유형", example = "INDEX_DATA")
    @NotNull(message = "작업 타입은 필수입니다.") // Enum이라서 NotNull
    JobType jobType,

    @Schema(description = "지수 정보 ID", example = "1")
    @NotNull(message = "지수 정보 ID는 필수입니다.") // Long이라서 NotNull
    Long indexInfoId,

    @Schema(description = "대상 날짜", example = "2026-03-13")
    @NotNull(message = "대상 날짜는 필수입니다.")
    LocalDate targetDate,

    @Schema(description = "작업자", example = "192.168.0.1")
    @NotBlank(message = "작업자 정보는 필수입니다.")
    String worker,

    @Schema(description = "작업 일시", example = "2026-03-13T11:00:00")
    @NotNull(message = "작업 일시는 필수입니다.") // LocalDateTime이라서 NotNull
    @PastOrPresent(message = "작업 일시는 미래일 수 없습니다.")
    LocalDateTime jobTime,

    @Schema(description = "연동 결과 상태", example = "NEW")
    @NotNull(message = "작업 결과는 필수입니다.") // Enum이라서 NotNull
    JobResult result
) {}
