package com.sprint.mission.findex.autosyncconfig.entity;


import com.sprint.mission.findex.base.BaseEntity;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 외부에서 무분별하게 new 하는 것을 막기
public class AutoSyncConfig extends BaseEntity {


  //지수 하나당 자동연동 설정은 하나만 존재
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexInfoId", nullable = false, unique = true)
  private IndexInfo indexInfo;

  private boolean enabled;

  @Builder
  public AutoSyncConfig(IndexInfo indexInfo, boolean enabled) {
    this.indexInfo = indexInfo;
    this.enabled = enabled;
  }

  //엔티티 내부에서 상태 변경
  public void update(boolean enabled) {
    this.enabled = enabled;
  }
}
