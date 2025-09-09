package com.example.bankcards.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String username;
    String role;
}
