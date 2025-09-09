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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }


    @Override
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){
        JwtDTO jwtDTO = authService.login(request);

        return ResponseEntity.ok().body(new JwtResponse(jwtDTO, "Пользователь вошёл успешно!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<JwtResponse> register(RegisterRequest request) {
        JwtDTO jwtDTO = authService.register(request);

        return ResponseEntity.ok().body(new JwtResponse(jwtDTO, "Пользователь зарегистрирован успешно!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Response<Void>> logout(LogoutRequest request) {
        authService.logout(request.getUsername());

        return ResponseEntity.ok().body(Response.of("Пользователь вышел успешно!", HttpStatus.OK));
    }


}
