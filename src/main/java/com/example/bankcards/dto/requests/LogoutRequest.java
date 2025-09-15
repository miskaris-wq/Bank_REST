package com.example.bankcards.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-запрос для выхода пользователя из системы.
 * <p>
 * Используется в методе {@code /api/v1/auth/logout}.
 * Содержит имя пользователя, инициирующего выход,
 * чтобы убедиться, что операция выполняется от имени текущего аутентифицированного пользователя.
 * </p>
 *
 * @author ksenya
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {

    /**
     * Имя пользователя, который выполняет выход из системы.
     */
    private String username;
}
