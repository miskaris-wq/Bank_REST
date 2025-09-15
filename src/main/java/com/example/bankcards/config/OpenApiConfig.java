package com.example.bankcards.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI/Swagger для проекта Bank Cards.
 *
 * <p>Содержит описание API, информацию о безопасности (JWT в заголовке)
 * и группировку эндпоинтов по доменным областям для удобной навигации
 * в Swagger UI.</p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Bank Cards API",
                version     = "1.0.0",
                description = "Документация API для управления банковскими картами"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    /**
     * Группа эндпоинтов для работы с банковскими картами.
     *
     * @return {@link GroupedOpenApi} с путями /api/v1/cards/**
     */
    @Bean
    public GroupedOpenApi cardApi() {
        return GroupedOpenApi.builder()
                .group("card")
                .pathsToMatch("/api/v1/cards/**")
                .build();
    }

    /**
     * Группа эндпоинтов для аутентификации.
     *
     * @return {@link GroupedOpenApi} с путями /api/v1/auth/**
     */
    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi.builder()
                .group("authentication")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    /**
     * Группа эндпоинтов для управления пользователями.
     *
     * @return {@link GroupedOpenApi} с путями /api/v1/user/**
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/v1/user/**")
                .build();
    }

    /**
     * Группа эндпоинтов для денежных переводов.
     *
     * @return {@link GroupedOpenApi} с путями /api/v1/transfer/**
     */
    @Bean
    public GroupedOpenApi transferApi() {
        return GroupedOpenApi.builder()
                .group("transfer")
                .pathsToMatch("/api/v1/transfer/**")
                .build();
    }

    /**
     * Группа эндпоинтов для запросов на блокировку карт.
     *
     * @return {@link GroupedOpenApi} с путями /api/v1/card-request/**
     */
    @Bean
    public GroupedOpenApi cardRequestApi() {
        return GroupedOpenApi.builder()
                .group("card requests")
                .pathsToMatch("/api/v1/card-request/**")
                .build();
    }
}
