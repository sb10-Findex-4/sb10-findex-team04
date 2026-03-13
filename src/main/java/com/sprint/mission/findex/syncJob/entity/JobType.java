package com.sprint.mission.findex.syncJob.entity;

public enum JobType {
  NEW,     // "접수됨/새로 생성됨" (Swagger JSON 예시 반영)
  SUCCESS, // "성공" ({결과} 설명 반영)
  FAILURE  // "실패" ({결과} 설명 반영)
}
