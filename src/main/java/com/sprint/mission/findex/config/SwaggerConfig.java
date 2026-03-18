package com.sprint.mission.findex.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Findex API") // 이미지의 큰 제목
            .version("v1.0.0")   // 이미지의 버전 태그
            .description("가볍고 빠른 외부 API 연동 금융 분석 도구 API 문서") // 이미지의 설명 문구
            .license(new License()
                .name("Apache 2.0") // 이미지 하단의 라이선스 정보
                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}
