package com.sprint.mission.findex.indexinfo.service;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoUpdateRequest;
import com.sprint.mission.findex.indexinfo.mapper.IndexInfoMapper;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import com.sprint.mission.findex.indexinfo.dto.request.IndexInfoCreateRequest;
import com.sprint.mission.findex.indexinfo.dto.response.IndexInfoDto;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoService {
    private final IndexInfoRepository indexInfoRepository;
    // private final IndexDataRepository indexDataRepository;
    // private final AutoSyncRepository autoSyncRepository;
    private final IndexInfoMapper indexInfoMapper;

    /*
    지수 정보 등록 (사용자 수동)
     */
    public IndexInfoDto createIndexInfo(IndexInfoCreateRequest request) {
        // 중복 검사
        boolean exists = indexInfoRepository.existsByIndexClassificationAndIndexName(
                request.indexClassification(),
                request.indexName()
        );

        if (exists) {
            throw new BusinessLogicException(ErrorCode.DUPLICATE_INDEX_INFO);
        }

        IndexInfo indexInfo = indexInfoMapper.toEntity(request);
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
    public IndexInfoDto getIndexInfoById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));
        return indexInfoMapper.toDto(indexInfo);
    }

    /*
    지수 정보 삭제
     */
    public void deleteIndexInfoById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));

        // TODO: IndexData 구현 후 삭제 되도록 추가 반영 필요 (Join vs Id로 삭제할 것인지?)
        //indexDataRepository.deleteByIndexInfoDataId(indexInfo.getId());
        indexInfoRepository.deleteById(indexInfo.getId());
    }

    /*
    지수 정보 수정 (사용자 수동)
     */
    public IndexInfoDto updateIndexInfoById(Long id, IndexInfoUpdateRequest request) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.INDEX_INFO_NOT_FOUND));

        indexInfo.update(request);

        IndexInfo savedIndexInfo = indexInfoRepository.save(indexInfo);
        return indexInfoMapper.toDto(savedIndexInfo);
    }

}
