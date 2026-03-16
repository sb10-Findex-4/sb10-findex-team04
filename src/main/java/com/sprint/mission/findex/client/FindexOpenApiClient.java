package com.sprint.mission.findex.client;

import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class FindexOpenApiClient {
    private final WebClient publicDataWebClient;        // WebCLinet 빈 객체 주입

    @Value("${springdoc.open-api.service-key}")         // 공공데이터포털에서 발급받은 인증 키
    private String serviceKey;

    /*
        지정된 날짜와 지수명에 해당하는 주식 정보를 외부 API로부터 비동기 호출
        ------------------------------------------------------
        @param baseDate 조회할 기준 날짜
        @param indexName 조회할 지수 명칭
        @return Mono<StockIndexResponse> 비동기 응답 객체
    */
    public Mono<StockMarketIndexResponseDto> fetchStockIndex(String baseDate, String indexName) {
        String encodedIndexName = UriUtils.encode(indexName, StandardCharsets.UTF_8);            // UTF-8 수동 인코딩

        return publicDataWebClient.get()                                                         // GET 요청 설정
                .uri(uriBuilder ->  uriBuilder
                            .path("/getStockMarketIndex")                                        // 지수 시세 정보 세부 경로
                            .queryParam("serviceKey", serviceKey)                          // 필수 인증키 파라미터를 추가합니다.
                            .queryParam("resultType", "json")                      // 응답 결과 타입을 JSON으로 지정합니다.
                            .queryParam("basDt", baseDate)                                 // 조회 일자를 필터로 설정합니다.
                            .queryParam("idxNm", encodedIndexName)                         // 조회 지수명을 필터로 설정합니다.
                            .build(true))                                            // true = 해당 uri가 인코딩 된 값임을 의미
                .retrieve()                                                                      // 응답 생성
                .bodyToMono(StockMarketIndexResponseDto.class)                                   // 응답 바디 -> DTO 클래스 변환
                .doOnError(e ->                                                        // 에러 메시지
                            System.out.println("Open API 호출 에러 : " + e.getMessage()));
    }
}
