package com.example.bankcards.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Конфигурация CORS для профиля {@code dev}.
 *
 * <p>Разрешает доступ к REST API с фронтенда, запущенного локально
 * по адресу {@code http://localhost:3000}.
 * Используется только в окружении разработки.</p>
 */
@Configuration
@Profile("dev")
public class DevCorsConfig {


    /**
     * Определяет CORS-настройки для dev-окружения.
     *
     * @return {@link CorsConfigurationSource} с разрешённым origin localhost:3000,
     * всеми методами и заголовками.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
