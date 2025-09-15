package com.example.bankcards.config;

import com.example.bankcards.entity.user.Role;
import com.example.bankcards.security.RestAccessDeniedHandler;
import com.example.bankcards.security.RestAuthenticationEntryPoint;
import com.example.bankcards.security.SecurityConstants;
import com.example.bankcards.security.filters.JwtAuthenticationFilter;
import com.example.bankcards.service.impl.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 *
 * <p>Использует JWT-аутентификацию, отключает хранение сессий (stateless),
 * включает CORS и методовую безопасность через {@link EnableMethodSecurity}.</p>
 *
 * <p>Основные настройки:</p>
 * <ul>
 *     <li>Открытые эндпоинты задаются в {@link SecurityConstants#PUBLIC_PATHS}.</li>
 *     <li>Админские эндпоинты доступны только с ролью {@link Role#ADMIN}.</li>
 *     <li>Остальные запросы требуют аутентификации.</li>
 *     <li>Фильтр {@link JwtAuthenticationFilter} добавляется после стандартного
 *         {@link UsernamePasswordAuthenticationFilter}.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailService userDetailService;
    private final RestAuthenticationEntryPoint restAuthEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailService userDetailService,
                          RestAuthenticationEntryPoint restAuthEntryPoint,
                          RestAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailService = userDetailService;
        this.restAuthEntryPoint = restAuthEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Основной фильтр безопасности: включает JWT, CORS и Stateless-сессии.
     *
     * @param httpSecurity объект для настройки HTTP-безопасности
     * @return готовый {@link SecurityFilterChain}
     * @throws Exception при ошибке конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {}) // настройки CORS подхватываются из CorsConfigurationSource
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SecurityConstants.PUBLIC_PATHS).permitAll()
                        .requestMatchers(SecurityConstants.ADMIN_PATHS).hasRole(Role.ADMIN.getRoleName())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(customAuthenticationProvider())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    /**
     * Менеджер аутентификации, используемый Spring Security.
     *
     * @param config конфигурация аутентификации
     * @return {@link AuthenticationManager}
     * @throws Exception при ошибке получения менеджера
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Провайдер аутентификации на основе DAO и {@link UserDetailService}.
     *
     * @return {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Шифратор паролей на основе BCrypt.
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
