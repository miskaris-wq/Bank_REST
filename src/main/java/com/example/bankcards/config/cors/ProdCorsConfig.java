package com.example.bankcards.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Конфигурация CORS для профиля {@code prod}.
 *
 * <p>Разрешает доступ к REST API только с боевого фронтенда
 * по адресу {@code https://my-frontend.com}.
 * Используется исключительно в production-среде.</p>
 */
@Configuration
@Profile("prod")
public class ProdCorsConfig {


    /**
     * Определяет CORS-настройки для prod-окружения.
     *
     * @return {@link CorsConfigurationSource} с разрешённым origin https://my-frontend.com,
     * ограниченным набором HTTP-методов (GET, POST, PUT, DELETE) и всеми заголовками.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://my-frontend.com");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
