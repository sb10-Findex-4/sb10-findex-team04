package com.sprint.mission.findex.syncjob.entity;

import com.sprint.mission.findex.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SyncJob extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "job_type", nullable = false)
  private JobType jobType;

  /* TODO: 지수 정보 엔티티(IndexInfo) 설계 완료 후 주석 해제 및 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", nullable = false)
    private IndexInfo indexInfo;
    */

  @Column(name = "target_date")
  private LocalDate targetDate;

  @Column(nullable = false, length = 100)
  private String worker;

  @Column(name = "job_time", nullable = false)
  private LocalDateTime jobTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private JobResult result;

  @Builder
  public SyncJob(JobType jobType/*, IndexInfo indexInfo, */, LocalDate targetDate,
      String worker, LocalDateTime jobTime, JobResult result) {
    this.jobType = jobType;
    //this.indexInfo = indexInfo;
    this.targetDate = targetDate;
    this.worker = worker;
    this.jobTime = jobTime;
    this.result = result;
  }



}
