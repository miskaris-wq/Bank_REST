package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.JwtDTO;
import com.example.bankcards.dto.requests.LoginRequest;
import com.example.bankcards.dto.requests.RegisterRequest;

/**
 * Сервис аутентификации и управления пользователями.
 * <p>
 * Отвечает за регистрацию, вход и выход пользователей,
 * а также за выпуск и отзыв JWT-токенов.
 * </p>
 *
 * @author ksenya
 */
public interface AuthService {

    /**
     * Выполняет аутентификацию пользователя по логину и паролю.
     *
     * @param request данные для входа (логин и пароль)
     * @return {@link JwtDTO} с JWT-токеном и временем истечения
     */
    JwtDTO login(LoginRequest request);

    /**
     * Регистрирует нового пользователя и выдает ему JWT-токен.
     *
     * @param request данные для регистрации (логин и пароль)
     * @return {@link JwtDTO} с JWT-токеном и временем истечения
     */
    JwtDTO register(RegisterRequest request);

    /**
     * Завершает сессию пользователя (инвалидация токена, logout).
     *
     * @param userName имя пользователя, который выходит из системы
     */
    void logout(String userName);
}
