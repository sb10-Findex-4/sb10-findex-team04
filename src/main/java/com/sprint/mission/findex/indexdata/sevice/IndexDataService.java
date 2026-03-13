package com.sprint.mission.findex.indexdata.sevice;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.entity.SourceType;
import com.sprint.mission.findex.indexdata.mapper.IndexDataMapper;
import com.sprint.mission.findex.indexdata.repository.IndexDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IndexDataService {
    private final IndexDataRepository indexDataRepository;
    private final IndexDataMapper indexDataMapper;

    /*
    지수 데이터 생성
     */
    public IndexDataDto create(IndexDataCreateRequestDto request) {
        // 중복 체크: (indexInfoId, baseDate) 조합이 중복되는지 검사
        boolean exist = indexDataRepository.existsByIndexInfoIdAndBaseDate(
                request.indexInfoId(),
                request.baseDate()
        );
        if (exist) {
            throw new BusinessLogicException(ErrorCode.DUPLICATE_INDEX_INFO);
        }

        IndexData indexData = indexDataMapper.toEntity(request);
        IndexData createdIndexData = indexDataRepository.save(indexData);

        return indexDataMapper.toDto(createdIndexData);
    }

    public IndexDataDto find(Long id) {
        return indexDataRepository.findById(id)
                .map(indexDataMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException());
    }

    /*
    지수 데이터 목록 조회
     */
    // TODO
    public List<IndexDataDto> findAll(IndexDataFindListRequestDto request) {
        indexDataRepository.findByIndexInfoIdAndBaseDateBetween(
                request.indexInfoId(),
                request.startDate(),
                request.endDate()
        );


    }

    /*
    지수 데이터 삭제
     */
    public void delete(Long id) {
        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        indexDataRepository.deleteById(id);
    }

    /*
    지수 데이터 수정
     */
    public IndexDataDto update(Long id, IndexDataUpdateRequestDto request) {
        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        indexData.update(request);
        IndexData updatedIndexData = indexDataRepository.save(indexData);
        return indexDataMapper.toDto(updatedIndexData);
    }
}
