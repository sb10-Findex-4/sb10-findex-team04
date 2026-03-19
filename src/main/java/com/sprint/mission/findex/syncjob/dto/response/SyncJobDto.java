package com.sprint.mission.findex.syncjob.dto.response;

import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@Schema(description = "연동 이력 상세 응답 DTO")

public record SyncJobDto (
    @Schema(description = "연동 이력 고유 ID", example = "1")
    Long id,

    @Schema(description = "연동 작업 유형", example = "INDEX_DATA")
    JobType jobType,

    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,

    @Schema(description = "대상 날짜", example = "2023-01-01")
    LocalDate targetDate,

    @Schema(description = "작업자", example = "192.168.0.1")
    String worker,

    @Schema(description = "작업 일시", example = "2023-01-01T12:00:00")
    LocalDateTime jobTime,

    @Schema(description = "연동 결과 상태", example = "NEW")
    JobResult result
) {
  // Entity를 DTO로 변환하는 정적 팩토리 메서드
  public static SyncJobDto from(SyncJob syncJob) {
    return SyncJobDto.builder()
        .id(syncJob.getId())
        .jobType(syncJob.getJobType())
        .indexInfoId(syncJob.getIndexInfo().getId())
        .targetDate(syncJob.getTargetDate())
        .worker(syncJob.getWorker())
        .jobTime(syncJob.getJobTime())
        .result(syncJob.getResult())
        .build();
  }
}
