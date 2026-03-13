package com.sprint.mission.findex.indexinfo.service;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexinfo.SourceType;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoSearchRequestDto;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.CursorPageResponseIndexInfoDto;
import com.sprint.mission.findex.indexinfo.mapper.IndexInfoMapper;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
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
        // 지수 분류명(부분 일치), 지수명(부분 일치), 즐겨찾기(완전일치)로 지수 정보 목록 필터링 -> TODO : 정렬까지 구현, QueryDSL 쿼리 최적화 필요해보임
        List<IndexInfo> indexInfos = indexInfoRepository.filter(
                request.indexClassification(),
                request.indexName(),
                request.favorite()
        );

        // 페이지네이션 구현 (현재 Id 기반)
        // 페이지 크기
        int size = 10;
        if (request.size() != null) {
            size = request.size();
        }

        // 페이지네이션 구현, size + 1 만큼 indexInfo 객체를 가져옴 (커서가 없을 경우 통과, 있다면 다음 id를 가져옴)
        List<IndexInfo> page = indexInfos.stream().filter(indexInfo -> request.isAfter() == null || indexInfo.getId() > request.isAfter())
                .limit(size + 1)
                .toList();

        // 마지막 요소 Id -> toString () Todo: 정렬 기반 페이지네이션 시 사용
        String nextCursor = null;
        if (!page.isEmpty()) {
            nextCursor = page.get(page.size() - 1).getId().toString();
        }

        // 마지막 요소 Id 가져오기
        Long nextIdAfter = null;
        if (!page.isEmpty()) {
            nextIdAfter = page.get(page.size() - 1).getId();
        }

        // 총 요소 수
        int totalElements = indexInfos.size();

        // 응답 Dto 변환
        List<IndexInfoDto> content = page.stream()
                .map(indexInfoMapper::toDto)
                .toList();

        return new CursorPageResponseIndexInfoDto<>(
                content,
                nextCursor,
                nextIdAfter,
                size,
                totalElements,
                content.size() > size // size가 10보다 크다면 다음 페이지가 있는 것
        );
    }

}
