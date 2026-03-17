package com.sprint.mission.findex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
/* 테스트 에러 임시 해결
@SpringBootTest(properties = {
    "springdoc.open-api.base-url=https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService",
    "springdoc.open-api.service-key=temp_key"
})
 */

class FindexApplicationTests {

    @Test
    void contextLoads() {
    }

}
