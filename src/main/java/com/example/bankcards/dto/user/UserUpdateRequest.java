package com.example.bankcards.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateRequest {
    // Если разрешишь смену пароля
    @Size(min = 6, max = 100)
    private String newPassword;

    // Для админа — смена роли
    private String role; // ADMIN | USER | null
}
