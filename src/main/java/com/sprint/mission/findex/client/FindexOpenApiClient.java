package com.sprint.mission.findex.client;

import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindexOpenApiClient {
    private final WebClient publicDataWebClient;        // WebCLinet 빈 객체 주입

    @Value("${springdoc.open-api.service-key}")         // 공공데이터포털에서 발급받은 인증 키
    private String serviceKey;

    /*
        [지수 정보]
        지정된 날짜에 해당하는 주식 정보를 외부 API로부터 비동기 호출
        ------------------------------------------------------
        @param baseDate 조회할 기준 날짜
        @return Mono<StockIndexResponse> 비동기 응답 객체
    */
    public Mono<StockMarketIndexResponseDto> fetchStockIndexInfo(String baseDate) {
        return publicDataWebClient.get()                                                         // GET 요청 설정
                .uri(uriBuilder ->  uriBuilder
                        .path("/getStockMarketIndex")                                           // 지수 시세 정보 세부 경로 설정
                        .queryParam("serviceKey", serviceKey)                             // 필수 파라미터인 공공데이터포털 인증키 추가
                        .queryParam("resultType", "json")                         // 응답 결과 타입 = JSON
                        .queryParam("numOfRows", 500)                             // 한 페이지 결과 수
                        .queryParam("basDt", baseDate)                                    // 조건 필터: 조회 일자
                        .build(true))                                               // true = 해당 uri가 인코딩 된 값임을 의미
                .retrieve()                                                                     // 응답 생성
                .bodyToMono(StockMarketIndexResponseDto.class)                                  // 응답 바디 -> DTO 클래스 변환
                .doOnError(e ->                                                       // 에러 메시지
                        System.out.println("Open API 호출 에러 : " + e.getMessage()));
    }

    /*
        [지수 데이터]
        지정된 날짜와 특정 지수 정보에 해당하는 주식 정보를 외부 API로부터 비동기 호출
        ------------------------------------------------------
        @param baseDate 조회할 기준 날짜
        @param IndexInfoId 조회할 지수 정보 ID
        @return Mono<StockIndexResponse> 비동기 응답 객체
    */
    public Mono<StockMarketIndexResponseDto> fetchStockIndexData(String baseDate, String indexName) {
        return publicDataWebClient.get()                                                     // GET 요청 설정
                .uri(uriBuilder ->  uriBuilder
                        .path("/getStockMarketIndex")                                        // 지수 시세 정보 세부 경로
                        .queryParam("serviceKey", serviceKey)                          // 필수 파라미터인 공공데이터포털 인증키 추가
                        .queryParam("resultType", "json")                      // 응답 결과 타입 = JSON
                        .queryParam("basDt", baseDate)                                 // 조건 필터: 조회 일자
                        .queryParam("indexName", indexName)                            // 조건 필터: 지수명
                        .build(true))                                            // true = 해당 uri가 인코딩 된 값임을 의미
                .retrieve()                                                                  // 응답 생성
                .bodyToMono(StockMarketIndexResponseDto.class)                               // 응답 바디 -> DTO 클래스 변환
                .doOnError(e ->                                                    // 에러 메시지
                        System.out.println("Open API 호출 에러 : " + e.getMessage()));
    }
}
