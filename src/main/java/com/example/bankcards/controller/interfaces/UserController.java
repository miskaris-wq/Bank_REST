package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.TotalBalanceResponse;
import com.example.bankcards.dto.Responses.UserResponse;
import com.example.bankcards.dto.Responses.UsersResponse;
import com.example.bankcards.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "Управление пользователями")
public interface UserController {

    @Operation(
            summary     = "Получить всех пользователей",
            description = "Возвращает страницу списка всех пользователей (только для администратора)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список пользователей получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = UsersResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    ResponseEntity<UsersResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );

    @Operation(
            summary     = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по его уникальному идентификатору (только для администратора)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Пользователь найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Пользователь с указанным ID не найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<UserResponse> getId(@PathVariable Long id);

    @Operation(
            summary     = "Обновить данные пользователя",
            description = "Обновляет информацию пользователя по его идентификатору (только для администратора)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Данные пользователя успешно обновлены",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Пользователь с указанным ID не найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<UserResponse> update(
            @PathVariable(name = "id") Long id,
            @RequestBody UserDTO userDTO
            );

    @Operation(
            summary     = "Удалить пользователя",
            description = "Удаляет пользователя по его идентификатору (только для администратора)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Пользователь успешно удалён",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Пользователь не найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id);

    @Operation(
            summary     = "Получить общий баланс пользователя",
            description = "Возвращает суммарный баланс всех карт указанного пользователя по его ID",
            responses   = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Баланс получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = TotalBalanceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: пользователь может смотреть только свой баланс",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Пользователь не найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{userId}/total-balance")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<TotalBalanceResponse> getTotalBalance(@PathVariable Long userId);

}