package com.sprint.mission.findex.indexdata.sevice;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexdata.dto.data.IndexDataCsvDto;
import com.sprint.mission.findex.indexdata.dto.data.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.reponse.CursorPageResponseIndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.entity.SortDirection;
import com.sprint.mission.findex.indexdata.entity.SourceType;
import com.sprint.mission.findex.indexdata.mapper.CursorPageResponseMapper;
import com.sprint.mission.findex.indexdata.mapper.IndexDataMapper;
import com.sprint.mission.findex.indexdata.repository.IndexDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IndexDataService {
    private final IndexDataRepository indexDataRepository;
    private final IndexDataMapper indexDataMapper;
    private final CursorPageResponseMapper cursorPageResponseMapper;

    /*
    지수 데이터 생성(수동)
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
    public CursorPageResponseIndexDataDto<IndexDataDto> findAll(IndexDataFindListRequestDto request) {
        // 1. Dto에 담긴 정렬 방향을 정렬 방식으로 설정
        Sort sort = request.sortDirection().equals("desc")
                ? Sort.by(request.sortField()).descending()
                : Sort.by(request.sortField()).ascending();

        // 2. 다음 페이지 유무 확인을 위해 가져올 페이지 개수를 기본 size보다 하나 더 큰 크기로 설정
        Pageable limit = PageRequest.of(0, request.size() + 1);

        List<IndexData> indexDatas;             // 레파지토리로부터 가져올 연동 작업(SyncJob) 저장 리스트

        Long idAfter = request.idAfter();       // Dto로부터 받은 다음 페이지 시작점

        // 3. 이전 페이지 유무에 따른 분기 설정
        if (idAfter == null) {
            // 첫 페이지: 필터 조건들만 적용하여 조회
            indexDatas = indexDataRepository.findFirstPageIndexDatas(request, limit);
        } else {
            // 다음 페이지: 특정 ID 이후 조건까지 포함하여 조회
            indexDatas = indexDataRepository.findNextPageIndexDatasById(request, idAfter, limit);
        }

        // 4. 다음 페이지 유무 확인 | 11개를 가져왔다면 다음 페이지가 존재하는 것
        boolean hasNext = indexDatas.size() > request.size();
        List<IndexData> pagedIndexDatas = hasNext
                ? indexDatas.subList(0, request.size())
                : indexDatas;

        // 5. 이전 페이지의 마지막 요소 ID 설정 = 다음 요청의 idAfter
        Long nextIdAfter = (hasNext && !pagedIndexDatas.isEmpty())
                ? pagedIndexDatas.get(pagedIndexDatas.size() - 1).getId()
                : null;

        // 6. 다음 페이지 시작점(cursor) 지정 = 다음 페이지의 cursor
        String nextCursor = (hasNext && ! pagedIndexDatas.isEmpty())
                ? pagedIndexDatas.get(pagedIndexDatas.size() - 1).getBaseDate().toString()
                : null;

        // 7. 연동 작업 (SyncJob) 엔티티 -> 응답 DTO 변환
        List<IndexDataDto> content = pagedIndexDatas.stream()
                .map(indexDataMapper::toDto)
                .toList();

        // 8. 응답 DTO -> 페이징 응답 DTO 반환
        return cursorPageResponseMapper.fromCursor(content, nextCursor, nextIdAfter, content.size(), hasNext);
    }

    /*
    지수 데이터 삭제(수동)
     */
    public void delete(Long id) {
        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        indexDataRepository.deleteById(id);
    }

    /*
    지수 데이터 수정(수동)
     */
    public IndexDataDto update(Long id, IndexDataUpdateRequestDto request) {
        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        indexData.update(request);
        IndexData updatedIndexData = indexDataRepository.save(indexData);
        return indexDataMapper.toDto(updatedIndexData);
    }

    /*
    지수 데이터 CSV Export
     */
    public void exportCsv(IndexDataExportRequestDto request, Writer writer) throws Exception{
        // Dto에 담긴 정렬 방향을 정렬 방식으로 설정
        Sort sort = request.sortDirection().equals("desc")
                ? Sort.by(request.sortField()).descending()
                : Sort.by(request.sortField()).ascending();

        // Repository 에서 조회된 IndexDat 리스트
        List<IndexData> indexDatas = indexDataRepository.findByIndexInfoIdAndBaseDateBetween(
                request.indexInfoId(),
                request.startDate(),
                request.endDate(),
                sort
        );

        // IndexData들을 CSVDto로 변환
        List<IndexDataCsvDto> csvDatas = indexDatas.stream()
                .map(data -> new IndexDataCsvDto(
                        data.getId(),
                        data.getIndexInfoId(),
                        data.getBaseDate(),
                        data.getSourceType(),
                        data.getMarketPrice(),
                        data.getClosingPrice(),
                        data.getHighPrice(),
                        data.getLowPrice(),
                        data.getVersus(),
                        data.getFluctuationRate(),
                        data.getTradingQuantity(),
                        data.getTradingPrice(),
                        data.getMarketTotalAmount()))
                .toList();

        // Writer 기반 CSV 생성기 생성
        StatefulBeanToCsv<IndexDataCsvDto> beanToCsv = new StatefulBeanToCsvBuilder<IndexDataCsvDto>(writer)
                .build();

        // Dto 리스트를 CSV로 작성
        beanToCsv.write(csvDatas);
    }
}
