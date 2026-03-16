package com.sprint.mission.findex.indexinfo.service;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexinfo.SourceType;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.CursorPageResponseIndexInfoDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoSummaryDto;
import com.sprint.mission.findex.indexinfo.mapper.IndexInfoMapper;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IndexInfoService {
    private final IndexInfoRepository indexInfoRepository;
    // private final IndexDataRepository indexDataRepository;
    // private final AutoSyncRepository autoSyncRepository;
    private final IndexInfoMapper indexInfoMapper;

    /*
    지수 정보 등록 (사용자 수동)
     */
    @Transactional
    public IndexInfoDto createIndexInfo(IndexInfoCreateRequestDto request) {
        // 중복 검사
        boolean exists = indexInfoRepository.existsByIndexClassificationAndIndexName(
                request.indexClassification(),
                request.indexName()
        );

        if (exists) {
            throw new BusinessLogicException(ErrorCode.DUPLICATE_INDEX_INFO);
        }

        IndexInfo indexInfo = indexInfoMapper.toEntity(request);
        indexInfo.updateSourceType(SourceType.USER);
        IndexInfo savedIndexInfo = indexInfoRepository.save(indexInfo);

        // TODO:자동 연동 설정 등록 및 저장

        /*
        자동 연동 설정은 비활성화 상태로 등록
         */
        return indexInfoMapper.toDto(savedIndexInfo);
    }

    /*
    지수 정보 조회 (단건 조회)
     */
    @Transactional(readOnly = true)
    public IndexInfoDto getIndexInfoById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));
        return indexInfoMapper.toDto(indexInfo);
    }

    /*
    지수 정보 삭제 -> TODO : 지수 데이터, 자동 연동 설정 삭제 O , 동기화 작업은 삭제 되면 안됨
     */
    @Transactional
    public void deleteIndexInfoById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));

        // TODO: IndexData 삭제
        //indexDataRepository.deleteByIndexInfo(indexInfo);

        // TODO: AutoSyncJob 삭제
        // autoSyncConfigRepository.deleteByIndexInfo(indexInfo);

        indexInfoRepository.deleteById(indexInfo.getId());
    }

    /*
    지수 정보 수정 (사용자 수동)
     */
    @Transactional
    public IndexInfoDto updateIndexInfoById(Long id, IndexInfoUpdateRequestDto request) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));

        indexInfo.update(request);

        IndexInfo savedIndexInfo = indexInfoRepository.save(indexInfo);
        return indexInfoMapper.toDto(savedIndexInfo);
    }

    /*
    Todo: 정렬 쿼리 고도화 필요함
    지수 정보 목록 조회 (필터링 O, 정렬 X, 커서 기반 페이지네이션 O)
    현 상태 : Id 기반 페이지네이션
    문제점 : Id 기반 말고 다른 방식으로 바꿔야 함
     */
    @Transactional(readOnly = true)
    public CursorPageResponseIndexInfoDto<IndexInfoDto> findAll(IndexInfoSearchRequestDto request) {
        // 페이지 크기 설정
        int size = request.size();

        // 정렬 기준 및 방향을 설정
        Sort sort = request.sortDirection().equals("asc") ? Sort.by(request.sortField()) : Sort.by(request.sortField()).descending();

        // 다음 페이지 유무 확인을 위해 size보다 1 크게 설정
        Pageable pageable = PageRequest.of(0, size + 1, sort);

        // 조건에 따른 필터링 + (size + 1) 만큼 객체를 가져옴
        List<IndexInfo> indexInfos = indexInfoRepository.filter(
                request.indexClassification(),
                request.indexName(),
                request.favorite(),
                pageable
        );

        // size 가 10보다 크면 10 크기로 자름
        boolean hasNext = indexInfos.size() > size;
        indexInfos = hasNext ? indexInfos.subList(0, size) : indexInfos;

        // Dto로 변환
        List<IndexInfoDto> content = indexInfos.stream()
                .map(indexInfoMapper::toDto)
                .toList();

        Long nextIdAfter = null; // 이전 페이지의 마지막 요소 Id
        String nextCursor = null; // 다음 페이지의 시작점
        if (hasNext) {
            nextIdAfter = indexInfos.get(size-1).getId();
            nextCursor = indexInfos.get(size-1).getIndexClassification();
        }

        int totalElements = indexInfos.size();

        return new CursorPageResponseIndexInfoDto<>(
                content,
                nextCursor,
                nextIdAfter,
                size,
                totalElements,
                hasNext
        );
    }

    @Transactional(readOnly = true)
    public List<IndexInfoSummaryDto> findAllSummaries() {
        List<IndexInfo> indexInfos = indexInfoRepository.findAll();
        return indexInfos.stream()
                .map(indexInfoMapper::toSummaryDto)
                .toList();
    }

}

