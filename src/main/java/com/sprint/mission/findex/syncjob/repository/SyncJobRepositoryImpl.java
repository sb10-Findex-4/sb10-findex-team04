package com.sprint.mission.findex.syncjob.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sprint.mission.findex.syncjob.entity.QSyncJob.syncJob;

@RequiredArgsConstructor
public class SyncJobRepositoryImpl implements SyncJobRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /*
        필터링 + 내림차순 정렬 + 커서 기반 페이지네이션이 적용된 연동 목록 조회
     */
    @Override
    public List<SyncJob> searchSyncJobs(SyncJobSearchConditionDto condition) {
        // 1. 동적 쿼리 내 WHERE 절에 추가될 빌더 객체 생성
        BooleanBuilder filterBuilder = new BooleanBuilder();

        // 2. 동적 쿼리 필터링 | 조회 조건이 존재할 경우, WHERE절에 추가

        // 작업 유형(jobType)
        if (condition.jobType() != null) {
            filterBuilder.and(syncJob.jobType.eq(JobType.valueOf(condition.jobType())));
        }

        // 지수 정보 ID(FK) << 연관관계 설정 후 주석 제거 예정
        if (condition.indexInfoId() != null) {
            // 연관 관계 설정 후, 주석 제거 예정
            // filterBuilder.and(syncJob.indexInfo.id.eq(condition.indexInfoId()));
        }

        // 대상 날짜(targetDate) | From ~ To
        if (condition.baseDateFrom() != null && condition.baseDateTo() != null) {
            // 시작 날짜와 종료 날짜가 함께 들어온 경우
            filterBuilder.and(syncJob.targetDate.between(condition.baseDateFrom(), condition.baseDateTo()));
        } else {
            // 시간 날짜와 종료 날짜가 각각 들어온 경우
            if (condition.baseDateFrom() != null) {
                filterBuilder.and(syncJob.targetDate.goe(condition.baseDateFrom()));
            }
            if (condition.baseDateTo() != null) {
                filterBuilder.and(syncJob.targetDate.loe(condition.baseDateTo()));
            }
        }

        // 작업자 키워드 포함 여부 확인
        if (condition.worker() != null) {
            filterBuilder.and(syncJob.worker.contains(condition.worker()));
        }

        // 작업 일시(jobTime)
        if (condition.jobTimeFrom() != null && condition.jobTimeTo() != null) {
            // 시작 시각과 종료 시각이 함께 들어온 경우
            filterBuilder.and(syncJob.jobTime.between(condition.jobTimeFrom(), condition.jobTimeTo()));
        } else {
            // 시작 시각과 종료 시각이 각각 들어온 경우
            if (condition.jobTimeFrom() != null) {
                filterBuilder.and(syncJob.jobTime.goe(condition.jobTimeFrom()));
            }
            if (condition.jobTimeTo() != null) {
                filterBuilder.and(syncJob.jobTime.loe(condition.jobTimeTo()));
            }
        }

        // 작업 결과 일치 여부 확인
        if (condition.status() != null) {
            filterBuilder.and(syncJob.result.eq(JobResult.valueOf(condition.status())));
        }

        // 3. 커서 기반 페이지네이션

        // 커서(cursor)와 이전 페이지의 마지막 요소 ID(idAfter)가 존재할 때만 WHERE 절에 빌더 객체 추가
        if (condition.cursor() != null && condition.idAfter() != null) {
            BooleanBuilder cursorBuilder = new BooleanBuilder();

            // 기본 기준 정렬 필드 = 작업 일시(jobTime)
            if ("jobTime".equals(condition.sortField())) {
                LocalDateTime cursorTime = LocalDateTime.parse(condition.cursor());

                // 커서 시간보다 과거(lt)인 데이터 조회
                cursorBuilder.or(syncJob.jobTime.lt(cursorTime));
                // 커서 시간과 시간은 같고(eq), ID가 커서 ID보다 작은(lt) 데이터 조회
                cursorBuilder.or(syncJob.jobTime.eq(cursorTime).and(syncJob.id.lt(condition.idAfter())));
            }
            // 기준 정렬 필드 = 대상 날짜(targetDate)
            else {
                LocalDate cursorDate = LocalDate.parse(condition.cursor());

                // 커서 날짜보다 과거(lt)인 데이터 조회
                cursorBuilder.or(syncJob.targetDate.lt(cursorDate));
                // 커서 날짜와 날짜는 같고(eq), ID가 커서 ID보다 작은(lt) 데이터 조회
                cursorBuilder.or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.lt(condition.idAfter())));
            }
            filterBuilder.and(cursorBuilder);
        }

        // 5. 정렬
        OrderSpecifier<?> orderSpecifier = "jobTime".equals(condition.sortField())
                ? syncJob.jobTime.desc()
                : syncJob.targetDate.desc();

        // 6. 동적 쿼리 실행
        return jpaQueryFactory
                .selectFrom(syncJob)
                .where(filterBuilder)                           // 필터링 (내림차순)
                .orderBy(orderSpecifier, syncJob.id.desc())     // 정렬 (오름차순) | 시간이 같을 경우, id 내림차순
                .limit(condition.size() + 1)                    // 다음 페이지 존재 여부 확인을 위한 + 1 반환
                .fetch();
    }

    /*
        필터링 조건이 적용된 연동 목록의 전체 개수 조회
     */
    @Override
    public long countWithFilter(SyncJobSearchConditionDto condition) {
        // 1. 동적 쿼리 내 WHERE 절에 추가될 빌더 객체 생성
        BooleanBuilder filterBuilder = new BooleanBuilder();

        // 2. 동적 쿼리 필터링
        if (condition.jobType() != null) {
            filterBuilder.and(syncJob.jobType.eq(JobType.valueOf(condition.jobType())));
        }

        if (condition.indexInfoId() != null) {
            // filterBuilder.and(syncJob.indexInfo.id.eq(condition.indexInfoId()));
        }

        if (condition.baseDateFrom() != null && condition.baseDateTo() != null) {
            // 시작 날짜와 종료 날짜가 함께 들어온 경우
            filterBuilder.and(syncJob.targetDate.between(condition.baseDateFrom(), condition.baseDateTo()));
        } else {
            // 시작 날짜와 종료 날짜가 각각 들어온 경우
            if (condition.baseDateFrom() != null) {
                filterBuilder.and(syncJob.targetDate.goe(condition.baseDateFrom()));
            }
            if (condition.baseDateTo() != null) {
                filterBuilder.and(syncJob.targetDate.loe(condition.baseDateTo()));
            }
        }

        if (condition.worker() != null) {
            filterBuilder.and(syncJob.worker.contains(condition.worker()));
        }

        if (condition.jobTimeFrom() != null && condition.jobTimeTo() != null) {
            // 시작 시각과 종료 시각이 함께 들어온 경우
            filterBuilder.and(syncJob.jobTime.between(condition.jobTimeFrom(), condition.jobTimeTo()));
        } else {
            // 시작 시각과 종료 시각이 각각 들어온 경우
            if (condition.jobTimeFrom() != null) {
                filterBuilder.and(syncJob.jobTime.goe(condition.jobTimeFrom()));
            }
            if (condition.jobTimeTo() != null) {
                filterBuilder.and(syncJob.jobTime.loe(condition.jobTimeTo()));
            }
        }

        if (condition.status() != null) {
            filterBuilder.and(syncJob.result.eq(JobResult.valueOf(condition.status())));
        }

        // 3. 동적 쿼리 적용
        Long totalCount = jpaQueryFactory
                .select(syncJob.count())
                .from(syncJob)
                .where(filterBuilder)
                .fetchOne();

        return totalCount != null ? totalCount : 0L;
    }
}