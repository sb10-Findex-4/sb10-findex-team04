package com.sprint.mission.findex.syncjob.service;

import com.sprint.mission.findex.syncjob.dto.request.SyncJobCreateRequestDto;
import com.sprint.mission.findex.syncjob.entity.JobResult;
import com.sprint.mission.findex.syncjob.entity.JobType;
import com.sprint.mission.findex.syncjob.mapper.SyncJobCursorPageResponseMapper;
import com.sprint.mission.findex.syncjob.mapper.SyncJobMapper;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncJobRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncJobService {
    private final SyncJobRepository syncJobRepository;

    private final SyncJobMapper syncJobMapper;
    private final SyncJobCursorPageResponseMapper cursorPageResponseMapper;
    /*
        연동 결과 생성 및 반환
     */
    public List<SyncJobDto> createSyncJob(SyncJobCreateRequestDto syncJobCreateRequestDto, String clientIp) {
        // 1. 제약 조건: 날짜 유효성 검사 (시작일이 종료일보다 뒤면 에러)
        if (syncJobCreateRequestDto.baseDateFrom().isAfter(syncJobCreateRequestDto.baseDateTo())) {
            throw new IllegalArgumentException("시작 날짜가 종료 날짜보다 미래일 수 없습니다.");
        }

        // 2. 작업자 정보 결정: IP가 있으면 IP 저장, 없으면 배치가 실행한 'system'으로 저장
        String worker =
            (syncJobCreateRequestDto.worker() == null || syncJobCreateRequestDto.worker().isBlank())
                ? "system"
                : syncJobCreateRequestDto.worker();

        // 3. 루프 안에서 매번 save 하지 않고 리스트에 모아서 한 번에 저장
        List<SyncJob> syncJobsToSave = new ArrayList<>();

        // 4. 대상 지수가 여러 개인 경우 지수별로 반복 처리 (Outer Loop)
        for (Long indexId : syncJobCreateRequestDto.indexInfoIds()) {

            // 대상 날짜가 여러 개인 경우 날짜별로 반복 처리 (Inner Loop)
            LocalDate currentDate = syncJobCreateRequestDto.baseDateFrom();

            while (!currentDate.isAfter(syncJobCreateRequestDto.baseDateTo())) {

                // 엔티티 생성: 넘겨받은 연동 결과와 식별된 정보를 조합
                SyncJob syncJob = SyncJob.builder()
                    .jobType(JobType.valueOf(syncJobCreateRequestDto.jobType()))    // 문자열 타입을 Enum으로 변환
                    .targetDate(currentDate)                                        // 루프 중인 현재 날짜 설정
                    .worker(worker)                                                 // 추출된 작업자 정보 설정
                    .jobTime(LocalDateTime.now())                                   // 현재 작업 일시 기록
                    .result(JobResult.SUCCESS)                                      // 기본적으로 성공으로 기록
                    // .indexInfo(indexInfoRepository.getReferenceById(indexId))    // 지수 엔티티 연결(준비 시 주석 해제)
                    .build();

                // 저장용 리스트에 추가
                syncJobsToSave.add(syncJob);

                // 다음 날짜로 이동
                currentDate = currentDate.plusDays(1);
            }
        }

        // 5. DB에 일괄 저장
        Iterable<SyncJob> savedEntities = syncJobRepository.saveAll(syncJobsToSave);

        // 6. 저장된 결과를 DTO 리스트로 변환하여 반환
        return StreamSupport.stream(savedEntities.spliterator(), false)
            .map(SyncJobDto::from)
            .toList();
    }

    /*
        연동 작업 목록 조회
     */
    public CursorPageResponseSyncJobDto<SyncJobDto> findAllSyncJobs(SyncJobSearchConditionDto condition) {
        // 1. 필터링 + 정렬 + 커서 기반 페이지네이션이 적용된 연동 작업 리스트
        List<SyncJob> syncJobs = syncJobRepository.searchSyncJobs(condition);

        // 2. 다음 페이지 유무 확인 | 11개를 가져왔다면 다음 페이지가 존재하는 것
        boolean hasNext = syncJobs.size() > condition.size();
        List<SyncJob> pagedSyncJobs = hasNext
                                      ? syncJobs.subList(0, condition.size())       // 있다면, 기본 페이지 크기 개수(10개)만큼 자르기
                                      : syncJobs;                                   // 없다면, 그대로 전달

        // 3. 이전 페이지의 마지막 요소 ID (다음 요청의 idAfter) 및 다음 페이지 시작점 (다음 페이지의 cursor) 설정
        Long nextIdAfter = null;
        String nextCursor = null;

        if (!pagedSyncJobs.isEmpty()) {
            SyncJob lastItem = pagedSyncJobs.get(pagedSyncJobs.size() - 1);

            // 다음 페이지가 존재할 때만 커서 및 마지막 요소 ID 전달
            if (hasNext) {
                nextIdAfter = lastItem.getId();
                nextCursor = "jobTime".equals(condition.sortField())
                        ? lastItem.getJobTime().toString()
                        : lastItem.getTargetDate().toString();
            }
        }

        // 7. 연동 작업 전체 개수 저장
        long totalElements = syncJobRepository.countWithFilter(condition);

        // 8. 연동 작업 (SyncJob) 엔티티 -> 응답 DTO 변환
        List<SyncJobDto> content = pagedSyncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();

        // 8. 응답 DTO -> 페이징 응답 DTO 반환
        return cursorPageResponseMapper.fromCursor(
                content,
                nextCursor,
                nextIdAfter,
                content.size(),
                totalElements,
                hasNext);
    }
}
