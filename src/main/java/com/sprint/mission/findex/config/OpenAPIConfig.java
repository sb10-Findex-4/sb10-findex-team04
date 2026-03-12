package com.sprint.mission.findex.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OpenAPIConfig {
    @Value("${springdoc.open-api.service-key}")
    private String serviceKey;

}