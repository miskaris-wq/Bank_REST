package com.example.bankcards.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация кэширования.
 *
 * <p>Аннотация {@link EnableCaching} включает механизм кэширования
 * Spring Cache на уровне всего приложения.
 * Конкретные кэши определяются аннотациями {@code @Cacheable}, {@code @CachePut}
 * и {@code @CacheEvict} в сервисах.</p>
 *
 * <p>Если в приложении подключён провайдер (например, Caffeine, EhCache, Redis),
 * Spring автоматически будет использовать его как бэкенд для хранения кэша.</p>
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
