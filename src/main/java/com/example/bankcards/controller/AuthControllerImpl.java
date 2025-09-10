package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.AuthController;
import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.LogoutRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.dto.Responses.JwtResponse;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) {
        JwtDTO tokenInfo = authService.login(loginRequest);
        JwtResponse response = new JwtResponse(tokenInfo, "Вход выполнен успешно", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<JwtResponse> register(RegisterRequest registerRequest) {
        JwtDTO tokenInfo = authService.register(registerRequest);
        JwtResponse response = new JwtResponse(tokenInfo, "Регистрация прошла успешно", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Response<Void>> logout(LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getUsername());
        Response<Void> response = Response.of("Выход из системы выполнен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
