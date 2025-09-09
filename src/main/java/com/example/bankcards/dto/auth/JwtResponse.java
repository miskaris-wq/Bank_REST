package com.example.bankcards.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class JwtResponse {
    String token;
    String tokenType;

    public static JwtResponse of(String token) {
        return JwtResponse.builder().token(token).tokenType("Bearer").build();
    }
}
