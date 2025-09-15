package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.payload.JwtDTO;
import com.example.bankcards.dto.requests.LoginRequest;
import com.example.bankcards.dto.requests.LogoutRequest;
import com.example.bankcards.dto.requests.RegisterRequest;
import com.example.bankcards.dto.response.APIResponse;
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

/**
 * Контроллер для операций аутентификации и авторизации.
 *
 * <p>Содержит методы для входа, регистрации и выхода из системы.
 * Работает с JWT-токенами для обеспечения stateless-безопасности.</p>
 */
@Tag(name = "Authentication", description = "Операции для входа в систему")
@RequestMapping("/api/v1/auth")
public interface AuthController {

    /**
     * Аутентификация пользователя по логину и паролю.
     *
     * @param loginRequest объект с логином и паролем
     * @return JWT-токен в обёртке {@link APIResponse}
     */
    @Operation(
            summary = "Авторизация",
            description = "Вход в систему по логину и паролю. Возвращает JWT-токен",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PostMapping("/login")
    ResponseEntity<APIResponse<JwtDTO>> login(@RequestBody LoginRequest loginRequest);

    /**
     * Регистрация нового пользователя.
     *
     * @param registerRequest данные для регистрации
     * @return JWT-токен нового пользователя
     */
    @Operation(
            summary = "Регистрация",
            description = "Создание нового пользователя. В ответ возвращает JWT-токен",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для регистрации",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PostMapping("/register")
    ResponseEntity<APIResponse<JwtDTO>> register(@RequestBody RegisterRequest registerRequest);

    /**
     * Завершение пользовательской сессии.
     *
     * <p>Может быть вызван только самим пользователем (проверяется через {@code @PreAuthorize}).</p>
     *
     * @param logoutRequest объект с именем пользователя
     * @return пустой {@link APIResponse} с сообщением об успешном выходе
     */
    @Operation(
            summary = "Выход",
            description = "Выход из системы. Завершает сессию пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Выход выполнен",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Запрещено выполнять выход за другого пользователя",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PostMapping("/logout")
    @PreAuthorize("#logoutRequest.username == authentication.principal.username")
    ResponseEntity<APIResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest);
}
