package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.JwtDTO;
import com.example.bankcards.dto.requests.LoginRequest;
import com.example.bankcards.dto.requests.RegisterRequest;

public interface AuthService {

    JwtDTO login(LoginRequest request);

    JwtDTO register(RegisterRequest request);

    void logout(String userName);
}