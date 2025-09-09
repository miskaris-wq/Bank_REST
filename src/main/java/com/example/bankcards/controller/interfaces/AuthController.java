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

@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Операции по входу в систему")
public interface AuthController {

    @Operation(
            summary = "Логин пользователя",
            description = "Принимает логин и пароль, возвращает JWT-токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный вход",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Неверный логин или пароль",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request);

    @Operation(
            summary = "Регистрация пользователя",
            description = "Принимает логин и пароль, возвращает JWT-токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная регистрация",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Не хватает логина или пароля",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/register")
    ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request);

    @Operation(
            summary = "Выход из системы",
            description = "Принимает имя пользователя и выполняет выход, очищая кеши и контекст безопасности",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный выход",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещён: попытка выхода за другого пользователя",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/logout")
    @PreAuthorize("#request.getUsername() == authentication.principal.username")
    ResponseEntity<Response<Void>> logout(@RequestBody LogoutRequest request);

}
