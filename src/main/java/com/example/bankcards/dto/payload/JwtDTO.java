package com.example.bankcards.dto.payload;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO для передачи информации о JWT-токене.
 *
 * <p>Используется в ответах при авторизации и регистрации,
 * содержит сам токен и дату его истечения.</p>
 */
@Data
@Builder
public class JwtDTO {

    /**
     * Сгенерированный JWT-токен.
     */
    private String token;

    /**
     * Дата и время окончания действия токена.
     */
    private Date expirationDate;
}
