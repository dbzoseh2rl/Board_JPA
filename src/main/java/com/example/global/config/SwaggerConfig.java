package com.example.global.config;

import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .info(new Info().title("Swagger Documents")
                        .description("스웨거 API 문서입니다.")
                        .version("v0.1"))
                .servers(List.of(
                                new Server().url("http://localhost:8080").description("Local")
                        ))
                .addSecurityItem(securityRequirement)
                .components(components);
    }

//    private static final String API_NAME = "Board Project API";
//    private static final String API_VERSION = "0.0.1";
//    private static final String API_DESCRIPTION = "Board API Specification";
//
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title(API_NAME)
//                        .version(API_VERSION)
//                        .description(API_DESCRIPTION));
//    }

}
