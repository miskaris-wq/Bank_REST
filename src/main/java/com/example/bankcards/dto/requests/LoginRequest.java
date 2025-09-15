package com.example.bankcards.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-запрос для аутентификации пользователя в системе.
 * <p>
 * Используется при отправке логина и пароля для получения JWT-токена.
 * Обычно применяется в методе {@code /api/v1/auth/login}.
 * </p>
 *
 * @author ksenya
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * Имя пользователя, введённое при входе.
     */
    private String username;

    /**
     * Пароль пользователя, введённый при входе.
     */
    private String password;
}
