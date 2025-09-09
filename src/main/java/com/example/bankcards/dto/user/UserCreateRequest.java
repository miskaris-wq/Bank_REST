package com.example.bankcards.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserCreateRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String username;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    // Для ADMIN-эндпоинта (иначе в сервисе ставь USER по умолчанию)
    private String role; // ADMIN | USER | null
}
