package com.example.bankcards.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Конфигурация WebMVC для приложения.
 *
 * <p>Включает:</p>
 * <ul>
 *     <li>Поддержку Spring Data Web (пагинация, сортировка),
 *         сериализация страниц через DTO ({@link EnableSpringDataWebSupport}).</li>
 *     <li>Управление транзакциями через {@link EnableTransactionManagement}.</li>
 *     <li>Перенаправление корневого URL {@code /} на Swagger UI.</li>
 * </ul>
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableTransactionManagement
public class WebConfig implements WebMvcConfigurer {

    /**
     * Регистрирует контроллеры представлений.
     *
     * <p>Корневой путь {@code /} автоматически перенаправляется
     * на Swagger UI: {@code /swagger-ui/index.html}.</p>
     *
     * @param registry реестр для регистрации view-контроллеров
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/")
                .setViewName("redirect:/swagger-ui/index.html");
    }
}
