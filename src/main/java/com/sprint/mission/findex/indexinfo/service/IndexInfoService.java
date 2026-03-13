package com.sprint.mission.findex.indexinfo.service;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexinfo.SourceType;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequestDto;
import com.sprint.mission.findex.indexinfo.mapper.IndexInfoMapper;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequestDto;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
