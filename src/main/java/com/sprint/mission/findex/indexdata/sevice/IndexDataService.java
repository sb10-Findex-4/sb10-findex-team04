package com.sprint.mission.findex.indexdata.sevice;

import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.entity.SourceType;
import com.sprint.mission.findex.indexdata.mapper.IndexDataMapper;
import com.sprint.mission.findex.indexdata.repository.IndexDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IndexDataService {
    private final IndexDataRepository indexDataRepository;
    private final IndexDataMapper indexDataMapper;

    public IndexData create(IndexDataCreateRequestDto request) {

        IndexData indexData = new IndexData(
                request.indexInfoId(),
                request.baseDate(),
                SourceType.USER,
                request.marketPrice(),
                request.closingPrice(),
                request.highPrice(),
                request.lowPrice(),
                request.versus(),
                request.fluctuationRate(),
                request.tradingQuantity(),
                request.tradingPrice(),
                request.marketToTalAmount()
        );

        IndexData createdIndexData = indexDataRepository.save(indexData);
        return indexData;
    }

    public IndexDataDto find(Long id) {

    }

    public void delete(Long id) {
        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        indexDataRepository.deleteById(id);
    }
}
