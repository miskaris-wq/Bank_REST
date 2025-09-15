package com.example.bankcards.security;

/**
 * Константы безопасности для настройки Spring Security.
 * <p>
 * Содержит списки публичных и административных эндпоинтов,
 * которые используются в {@link com.example.bankcards.config.SecurityConfig}.
 * </p>
 */
public final class SecurityConstants {

    /**
     * Приватный конструктор, чтобы запретить создание экземпляров утилитарного класса.
     */
    private SecurityConstants() {}

    /**
     * Пути, доступные без аутентификации (публичные ресурсы).
     * <p>
     * Включают эндпоинты для авторизации, регистрации и документации Swagger.
     * </p>
     */
    public static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/v3/api-docs.yaml/**",
            "/v3/api-docs/**",
            "/docs/openapi.yaml",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/"
    };

    /**
     * Пути, доступные только пользователям с ролью ADMIN.
     * <p>
     * Сюда входят эндпоинты для управления картами, пользователями,
     * переводами и заявками на блокировку.
     * </p>
     */
    public static final String[] ADMIN_PATHS = {
            "/api/v1/card/create",
            "/api/v1/card/all",
            "/api/v1/card/all/{userId}",
            "/api/v1/card/blocked/{id}",
            "/api/v1/card/activate/{id}",
            "/api/v1/card/{id}",
            "/api/v1/user/{id}",
            "/api/v1/user/all",
            "/api/v1/transfer/{id}",
            "/api/v1/transfer/all",
            "/api/v1/card-request/rejected/{id}",
            "/api/v1/card-request/all",
    };
}
