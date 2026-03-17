package com.sprint.mission.findex.indexdata.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;
import com.sprint.mission.findex.indexdata.dto.response.ChartDataPoint;
import com.sprint.mission.findex.indexdata.dto.response.IndexChartDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexDataCsvDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexDataDto;
import com.sprint.mission.findex.indexdata.dto.response.CursorPageResponseIndexDataDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataCreateRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataExportRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataFindListRequestDto;
import com.sprint.mission.findex.indexdata.dto.request.IndexDataUpdateRequestDto;
import com.sprint.mission.findex.indexdata.dto.response.IndexPerformanceDto;
import com.sprint.mission.findex.indexdata.dto.response.RankedIndexPerformanceDto;
import com.sprint.mission.findex.indexdata.entity.IndexData;
import com.sprint.mission.findex.indexdata.mapper.IndexDataCursorPageResponseMapper;
import com.sprint.mission.findex.indexdata.mapper.IndexDataMapper;
import com.sprint.mission.findex.indexdata.repository.IndexDataRepository;
import com.sprint.mission.findex.indexinfo.entity.IndexInfo;
import com.sprint.mission.findex.indexinfo.repository.IndexInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final IndexDataCursorPageResponseMapper cursorPageResponseMapper;
    private final IndexInfoRepository indexInfoRepository;

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
        Pageable limit = PageRequest.of(0, request.size() + 1, sort);

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
        indexDataRepository.findById(id)
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
    public void exportCsv(IndexDataExportRequestDto request, Writer writer) throws Exception {
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
        StatefulBeanToCsv<IndexDataCsvDto> beanToCsv = new StatefulBeanToCsvBuilder<IndexDataCsvDto>(
            writer)
            .build();

        // Dto 리스트를 CSV로 작성
        beanToCsv.write(csvDatas);
    }

    /*
    [대시보드] 지수 차트 조회
     */
    public IndexChartDto getIndexChart(Long id, String period) {
        // 1. 기간 계산 (1달/3달/1년)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(period);

        // 2. MA20을 위해 시작일보다 30일 이전의 데이터를 가져옴 (주말/공휴일 고려)
        List<IndexData> allData = indexDataRepository.findByIndexInfoIdAndBaseDateBetweenOrderByBaseDateAsc(
            id, startDate.minusDays(30), endDate);

        // 3. 지수 기본 정보 조회
        IndexInfo info = indexInfoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("지수 정보를 찾을 수 없습니다."));

        // 4. 이동평균선 계산
        List<ChartDataPoint> dataPoints = new ArrayList<>();
        List<ChartDataPoint> ma5Points = new ArrayList<>();
        List<ChartDataPoint> ma20Points = new ArrayList<>();

        for (int i = 0; i < allData.size(); i++) {
            IndexData current = allData.get(i);
            LocalDate date = current.getBaseDate();

            // 실제 사용자가 요청한 기간(startDate) 이후 데이터만 결과 리스트에 담음
            if (!date.isBefore(startDate)) {
                double price = current.getClosingPrice().doubleValue();
                dataPoints.add(new ChartDataPoint(date, price));

                // MA5 계산 (현재 포함 과거 5개)
                if (i >= 4) {
                    double ma5 = allData.subList(i - 4, i + 1).stream()
                        .mapToDouble(d -> d.getClosingPrice().doubleValue()).average().orElse(0);
                    ma5Points.add(new ChartDataPoint(date, ma5));
                }

                // MA20 계산 (현재 포함 과거 20개)
                if (i >= 19) {
                    double ma20 = allData.subList(i - 19, i + 1).stream()
                        .mapToDouble(d -> d.getClosingPrice().doubleValue()).average().orElse(0);
                    ma20Points.add(new ChartDataPoint(date, ma20));
                }
            }
        }

        return new IndexChartDto(id, info.getIndexClassification(), info.getIndexName(), period,
            dataPoints, ma5Points, ma20Points);
    }

    // 차트 조회 기간(period)을 기준으로 DB 조회 시작일 계산
    private LocalDate calculateStartDate(String period) {
        // null 방지 및 대문자 변환 후 기간별 날짜 계산
        return switch (period.toUpperCase()) {
            case "3M" -> LocalDate.now().minusMonths(3);
            case "1Y" -> LocalDate.now().minusYears(1);
            default -> LocalDate.now().minusMonths(1); // 기본 1M
        };
    }

    /*
    [대시보드] 지수 성과 랭킹 조회
     */
    public List<RankedIndexPerformanceDto> getIndexRankings(String period, String classification) {

        // 1. 기준 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate baseDate = calculateRankBaseDate(period);

        // 2. 데이터 조회 (분류 필터 있다면 적용)
        List<IndexData> currentData = indexDataRepository.findByBaseDate(today);
        List<IndexData> pastData = indexDataRepository.findByBaseDate(baseDate);

        // 지수 정보 전체 조회 (분류 필터링 및 이름 매핑용)
        Map<Long, IndexInfo> infoMap = indexInfoRepository.findAll().stream()
            .filter(info -> classification == null || info.getIndexClassification().equals(classification))
            .collect(Collectors.toMap(IndexInfo::getId, i -> i));

        Map<Long, IndexData> pastDataMap = pastData.stream()
            .collect(Collectors.toMap(IndexData::getIndexInfoId, d -> d));

        List<IndexPerformanceDto> performanceList = new ArrayList<>();

        for (IndexData current : currentData) {
            IndexInfo info = infoMap.get(current.getIndexInfoId());
            IndexData past = pastDataMap.get(current.getIndexInfoId());

            // 지수 정보가 있고, 기준일 데이터도 있는 경우만 계산
            if (info != null && past != null) {
                BigDecimal curPrice = current.getClosingPrice();
                BigDecimal oldPrice = past.getClosingPrice();

                // 컬럼 계산
                BigDecimal versus = curPrice.subtract(oldPrice); // 대비

                // 0으로 나누기 방지 로직
                if (oldPrice.compareTo(BigDecimal.ZERO) != 0) {
                    double rate = versus.divide(oldPrice, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();

                    performanceList.add(new IndexPerformanceDto(
                        info.getId(),
                        info.getIndexClassification(),
                        info.getIndexName(),
                        versus.doubleValue(),
                        rate,
                        curPrice.doubleValue(), // 현재
                        oldPrice.doubleValue()  // 이전
                    ));
                }
            }
        }

        // 3. 등락률 기준 내림차순 정렬
        performanceList.sort(Comparator.comparingDouble(IndexPerformanceDto::fluctuationRate).reversed());

        // 4. 랭킹 부여
        List<RankedIndexPerformanceDto> rankedResults = new ArrayList<>();
        for (int i = 0; i < performanceList.size(); i++) {
            rankedResults.add(new RankedIndexPerformanceDto(performanceList.get(i), i + 1));
        }

        return rankedResults;
    }

    // 차트 조회 기간(period)을 기준으로 DB 조회 시작일 계산
    private LocalDate calculateRankBaseDate(String period) {
        return switch (period.toUpperCase()) {
            case "1D" -> LocalDate.now().minusDays(1);       // 일간 (전일 대비)
            case "1W" -> LocalDate.now().minusWeeks(1);     // 주간 (전주 대비)
            case "1M" -> LocalDate.now().minusMonths(1);   // 월간 (전월 대비)
            default -> LocalDate.now().minusMonths(1);     // 기본값 1M (월간)
        };
    }
}
