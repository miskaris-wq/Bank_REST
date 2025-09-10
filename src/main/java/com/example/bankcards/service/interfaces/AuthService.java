package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;

public interface AuthService {

    JwtDTO login(LoginRequest request);

    JwtDTO register(RegisterRequest request);

    void logout(String userName);
}