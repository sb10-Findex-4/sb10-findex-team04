package com.sprint.mission.findex.indexinfo.service;

import com.sprint.mission.findex.client.FindexOpenApiClient;
import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
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
import reactor.core.publisher.Mono;

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
    지수 정보 삭제 -> TODO : 지수 데이터, 자동 연동 설정 삭제 O , 연동 작업은 삭제 되면 안됨
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
    지수 정보 목록 조회 (필티렁, 정렬, 커서 기반 페이지네이션 + QueryDSL)
     */
    @Transactional(readOnly = true)
    public CursorPageResponseIndexInfoDto<IndexInfoDto> findAll(IndexInfoSearchRequestDto request) {
        // 필터링 + 정렬 + 페이지네이션
        List<IndexInfo> indexInfos = indexInfoRepository.filter(request);

        // size 가 10보다 크면 10 크기로 자름
        boolean hasNext = indexInfos.size() > request.size();
        indexInfos = hasNext ? indexInfos.subList(0, request.size()) : indexInfos;

        // Dto로 변환
        List<IndexInfoDto> content = indexInfos.stream()
                .map(indexInfoMapper::toDto)
                .toList();

        Long nextIdAfter = null; // 이전 페이지의 마지막 요소 Id
        String nextCursor = null; // 다음 페이지의 시작점
        if (hasNext) { // 다음 요소가 있을 경우
            IndexInfo last = indexInfos.get(request.size()-1); // 마지막 요소 get
            nextIdAfter = last.getId(); // 마지막 요소의 id를 가져옴

            nextCursor =
                    // sortField 조건에 따라 분기
                    switch (request.sortField()){
                        case "indexName" -> last.getIndexName();
                        case "employedItemsCount" -> String.valueOf(last.getEmployedItemsCount());
                        default -> last.getIndexClassification();
            };
        }

        int size = indexInfos.size();

        // 전체 개수 조회
        int totalElements = indexInfoRepository.count(request);

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

