package com.example.bankcards.security;

public final class SecurityConstants {

    private SecurityConstants() {}

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
