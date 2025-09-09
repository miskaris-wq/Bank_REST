package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /** Маппинг сущности в DTO ответа (наружу без пароля). */
    public UserResponse toResponse(@Nullable User u) {
        if (u == null) return null;
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getRole() != null ? u.getRole().name() : null)
                .build();
    }

    /**
     * Фабрика сущности для создания пользователя.
     * Пароль передаётся уже захешированным (шифрование/хеш — в сервисе).
     */
    public User fromCreate(String username, String passwordHash, Role role) {
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(passwordHash);
        u.setRole(role);
        return u;
    }
}
