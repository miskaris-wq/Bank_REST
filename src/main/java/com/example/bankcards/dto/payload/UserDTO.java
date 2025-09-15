package com.example.bankcards.dto.payload;

import com.example.bankcards.entity.user.Role;
import lombok.Data;

import java.util.Set;

/**
 * DTO для передачи информации о пользователе.
 *
 * <p>Используется для возврата и приёма данных о пользователях
 * в контроллерах и сервисах.</p>
 */
@Data
public class UserDTO {

    /**
     * Уникальное имя пользователя (логин).
     */
    private String username;

    /**
     * Пароль пользователя (как правило, хранится в зашифрованном виде).
     */
    private String password;

    /**
     * Набор ролей пользователя (например: USER, ADMIN).
     */
    private Set<Role> roles;
}
