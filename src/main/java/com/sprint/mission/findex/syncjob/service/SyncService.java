package com.sprint.mission.findex.syncjob.service;

import com.sprint.mission.findex.syncjob.mapper.CursorPageResponseMapper;
import com.sprint.mission.findex.syncjob.mapper.SyncJobMapper;
import com.sprint.mission.findex.syncjob.dto.request.SyncJobSearchConditionDto;
import com.sprint.mission.findex.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.sprint.mission.findex.syncjob.dto.response.SyncJobDto;
import com.sprint.mission.findex.syncjob.entity.SyncJob;
import com.sprint.mission.findex.syncjob.repository.SyncRepository;
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
public class SyncService {
    private final SyncRepository syncRepository;

    private final SyncJobMapper syncJobMapper;
    private final CursorPageResponseMapper cursorPageResponseMapper;

    /*
        연동 작업 목록 조회
     */
    public CursorPageResponseSyncJobDto<SyncJobDto> findAllSyncJobs(SyncJobSearchConditionDto syncJobSearchConditionDto) {
        // 1. Dto에 담긴 정렬 방향을 정렬 방식으로 설정 (기본값: DESC)
        Sort sort = syncJobSearchConditionDto.sortDirection().equalsIgnoreCase("desc")
                ? Sort.by(syncJobSearchConditionDto.sortField()).descending()
                : Sort.by(syncJobSearchConditionDto.sortField()).ascending();

        // 2. 다음 페이지 유무 확인을 위해 가져올 페이지 개수를 기본 size보다 하나 더 큰 크기로 설정
        Pageable limit = PageRequest.of(0, syncJobSearchConditionDto.size() + 1, sort);

        List<SyncJob> syncJobs;                                 // 레파지토리로부터 가져올 연동 작업(SyncJob) 저장 리스트
        Long idAfter = syncJobSearchConditionDto.idAfter();     // Dto로부터 받은 다음 페이지 시작점

        // 3. 이전 페이지 유무에 따른 분기 설정
        if (idAfter == null) {
            // 첫 페이지: 필터 조건들만 적용하여 조회
            syncJobs = syncRepository.findFirstPageSyncJobs(syncJobSearchConditionDto, limit);
        } else {
            // 다음 페이지: 특정 ID 이후 조건까지 포함하여 조회
            syncJobs = syncRepository.findNextPageSyncJobsById(syncJobSearchConditionDto, idAfter, limit);
        }

        // 4. 다음 페이지 유무 확인 | 11개를 가져왔다면 다음 페이지가 존재하는 것
        boolean hasNext = syncJobs.size() > syncJobSearchConditionDto.size();
        List<SyncJob> pagedSyncJobs = hasNext
                                      ? syncJobs.subList(0, syncJobSearchConditionDto.size())   // 있다면, 기본 페이지 크기 개수(10개)만큼 자르기
                                      : syncJobs;                                               // 없다면, 그대로 전달

        // 5. 이전 페이지의 마지막 요소 ID 설정 = 다음 요청의 idAfter
        Long nextIdAfter = (hasNext && !pagedSyncJobs.isEmpty())
                ? pagedSyncJobs.get(pagedSyncJobs.size() - 1).getId()
                : null;

        // 6. 다음 페이지 시작점(cursor) 지정 = 다음 페이지의 cursor
        String nextCursor = (hasNext && !pagedSyncJobs.isEmpty())
                ? pagedSyncJobs.get(pagedSyncJobs.size() - 1).getJobTime().toString()
                : null;

        // 7. 연동 작업 전체 개수 저장
        long totalElements = syncRepository.countWithFilter(syncJobSearchConditionDto);

        // 8. 연동 작업 (SyncJob) 엔티티 -> 응답 DTO 변환
        List<SyncJobDto> content = pagedSyncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();

        // 8. 응답 DTO -> 페이징 응답 DTO 반환
        return cursorPageResponseMapper.fromCursor(content, nextCursor, nextIdAfter, content.size(), totalElements, hasNext);
    }
}
