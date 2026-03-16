package com.sprint.mission.findex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

/*
    WebClient 설정
    --------------
    외부 API 통신에 사용할 WebClient 빈 생성
 */
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient publicDataWebClient(@Value("${springdoc.open-api.base-url}") String baseUrl) {
        // 1. 인증키 원본 유지를 위해 WebClient의 파라미터 인코딩 방지
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // 2. WebClient 빈 객체 반환
        return WebClient.builder()
                .uriBuilderFactory(factory)                                                     // 인코딩 모드 = NONE
                .baseUrl(baseUrl)                                                               // API 기본 도메인 주소
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)      // 기본 응답 형식 = JSON
                .build();
    }
}
