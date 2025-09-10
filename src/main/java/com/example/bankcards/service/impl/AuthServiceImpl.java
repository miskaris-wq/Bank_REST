package com.example.bankcards.service.impl;

import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.MissingCredentialsException;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtComponent;
import com.example.bankcards.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtComponent jwtComponent;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(cacheNames = "login", key = "#request.userName")
    public JwtDTO login(LoginRequest request) {
        if (request.getUserName() == null || request.getPassword() == null) {
            throw new MissingCredentialsException("Не указан логин или пароль");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Неверные логин или пароль");
        }

        var token = jwtComponent.generateJwtToken(request.getUserName());

        return JwtDTO.builder()
                .token(token)
                .expirationDate(jwtComponent.extractExpiration(token))
                .build();
    }

    @Cacheable(cacheNames = "register", key = "#request.userName")
    public JwtDTO register(RegisterRequest request) {
        if (request.getUserName() == null || request.getPassword() == null) {
            throw new MissingCredentialsException("Не указан логин или пароль");
        }

        userRepository.findByUsername(request.getUserName())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(
                            "Пользователь с логином " + request.getUserName() + " уже существует"
                    );
                });

        var user = User.builder()
                .username(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankCartList(Collections.emptyList())
                .build();

        userRepository.save(user);

        var token = jwtComponent.generateJwtToken(user.getUsername());

        return JwtDTO.builder()
                .token(token)
                .expirationDate(jwtComponent.extractExpiration(token))
                .build();
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "login", key = "#userName"),
            @CacheEvict(cacheNames = "register", key = "#userName")
    })
    public void logout(String userName) {
        SecurityContextHolder.clearContext();
        log.info("Пользователь {} успешно вышел из системы", userName);
    }
}
