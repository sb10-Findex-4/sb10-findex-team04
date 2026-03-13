package com.sprint.mission.findex.indexdata.mapper;

import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.springframework.stereotype.Component;

@Component
public class IndexDataMapper {

    public IndexDataDto toDto(IndexData indexData) {
        if (indexData == null)
            return null;

        return new IndexDataDto(
                indexData.getId(),
                indexData.getIndexInfoId(),
                indexData.getBaseDate(),
                indexData.getSourceType(),
                indexData.getMarketPrice(),
                indexData.getClosingPrice(),
                indexData.getHighPrice(),
                indexData.getLowPrice(),
                indexData.getVersus(),
                indexData.getFluctuationRate(),
                indexData.getTradingQuantity(),
                indexData.getTradingPrice(),
                indexData.getMarketTotalAmount()
        );
    }
}
