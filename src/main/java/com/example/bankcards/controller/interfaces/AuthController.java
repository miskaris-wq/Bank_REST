package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.LogoutRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.dto.Responses.JwtResponse;
import com.example.bankcards.dto.Responses.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Операации для входа в систему")
@RequestMapping("/api/v1/auth")
public interface AuthController {

    @Operation(
            summary = "Авторизация",
            description = "Вход в систему по логину и паролю. Возвращает JWT-токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный вход",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверные учетные данные",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    )
            }
    )
    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest);

    @Operation(
            summary = "Регистрация",
            description = "Создание нового пользователя. В ответ возвращает JWT-токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные для регистрации",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    )
            }
    )
    @PostMapping("/register")
    ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest);

    @Operation(
            summary = "Выход",
            description = "Выход из системы. Завершает сессию пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Выход выполнен",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Запрещено выполнять выход за другого пользователя",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    )
            }
    )
    @PostMapping("/logout")
    @PreAuthorize("#logoutRequest.username == authentication.principal.username")
    ResponseEntity<Response<Void>> logout(@RequestBody LogoutRequest logoutRequest);
}
