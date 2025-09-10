package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.AuthController;
import com.example.bankcards.dto.payload.JwtDTO;
import com.example.bankcards.dto.requests.LoginRequest;
import com.example.bankcards.dto.requests.LogoutRequest;
import com.example.bankcards.dto.requests.RegisterRequest;
import com.example.bankcards.dto.response.APIResponse;
import com.example.bankcards.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authServiceImpl;

    public AuthControllerImpl(AuthService authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @Override
    public ResponseEntity<APIResponse<JwtDTO>> login(LoginRequest loginRequest) {
        JwtDTO tokenInfo = authServiceImpl.login(loginRequest);
        return ResponseEntity.ok(
                APIResponse.ofSuccess(tokenInfo, "Вход выполнен успешно", HttpStatus.OK)
        );
    }

    @Override
    public ResponseEntity<APIResponse<JwtDTO>> register(RegisterRequest registerRequest) {
        JwtDTO tokenInfo = authServiceImpl.register(registerRequest);
        return ResponseEntity.ok(
                APIResponse.ofSuccess(tokenInfo, "Регистрация прошла успешно", HttpStatus.OK)
        );
    }

    @Override
    public ResponseEntity<APIResponse<Void>> logout(LogoutRequest logoutRequest) {
        authServiceImpl.logout(logoutRequest.getUsername());
        return ResponseEntity.ok(
                APIResponse.ofSuccess(null, "Выход из системы выполнен", HttpStatus.OK)
        );
    }
}
