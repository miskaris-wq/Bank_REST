package com.example.bankcards.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-запрос для регистрации нового пользователя.
 * <p>
 * Используется в методе {@code /api/v1/auth/register}.
 * Содержит данные, необходимые для создания нового пользователя в системе —
 * имя пользователя и пароль.
 * </p>
 *
 * @author ksenya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * Имя пользователя, под которым он будет зарегистрирован в системе.
     */
    private String username;

    /**
     * Пароль пользователя (в незашифрованном виде, будет хэшироваться при сохранении).
     */
    private String password;
}
