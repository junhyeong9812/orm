package com.benchmark.orm.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ORM 비교 프로젝트 API")
                        .description("JPA, MyBatis, QueryDSL 테스트용 API 문서입니다.")
                        .version("v1.0.0"));
    }
}
