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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailService userDetailService;
    private final RestAuthenticationEntryPoint restAuthEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;



    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailService userDetailService, RestAuthenticationEntryPoint restAuthEntryPoint, RestAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailService = userDetailService;
        this.restAuthEntryPoint = restAuthEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
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

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
