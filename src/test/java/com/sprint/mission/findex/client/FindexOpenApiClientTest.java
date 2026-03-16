package com.sprint.mission.findex.client;

import com.sprint.mission.findex.client.dto.StockMarketIndexResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("dev")                                                      // application-dev.yaml 파일 적용
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FindexOpenApiClientTest {
    private final FindexOpenApiClient findexOpenApiClient;

    // 생성자 주입
    FindexOpenApiClientTest(FindexOpenApiClient findexOpenApiClient) {
        this.findexOpenApiClient = findexOpenApiClient;
    }

    @Test
    @DisplayName("외부 API 데이터 수신 및 DTO 매핑 테스트")
    void testFetchStockIndex() {
        // 1. 테스트용 입력 데이터 설정
        String testDate = "20240313";
        String testIndex = "코스피";

        // 2. 외부 API 호출 및 응답 데이터 수신
        StockMarketIndexResponseDto result = findexOpenApiClient.fetchStockIndexInfo(testDate).block();

        // 3. 응답 객체 및 바디의 Null 여부 검증
        assertNotNull(result);
        assertNotNull(result.response());
        assertNotNull(result.response().body());

        // 4. API 서버 응답 메시지 결과 확인
        System.out.println("Result Message: " + result.response().header().resultMsg());

        // 5. 실제 지수 데이터 리스트 존재 여부 확인
        var items = result.response().body().items().item();
        assertNotNull(items);
        assertFalse(items.isEmpty(), "데이터 리스트가 비어있습니다.");

        // 6. 개별 필드 데이터 매핑 상태 확인 및 출력
        StockMarketIndexResponseDto.IndexItem firstItem = items.get(0);
        System.out.println("=== API Response Data ===");
        System.out.println("Base Date: " + firstItem.baseDate());
        System.out.println("Index Name: " + firstItem.indexName());
        System.out.println("Closing Price: " + firstItem.closingPrice());
        System.out.println("Fluctuation Rate: " + firstItem.fluctuationRate());
        System.out.println("Trading Volume: " + firstItem.tradingVolume());

        assertNotNull(firstItem.baseDate());
        assertNotNull(firstItem.closingPrice());
    }
}