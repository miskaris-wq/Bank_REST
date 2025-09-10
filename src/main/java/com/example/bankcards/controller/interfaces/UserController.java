package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.payload.UserDTO;
import com.example.bankcards.dto.payload.TotalCardBalanceDTO;
import com.example.bankcards.dto.response.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
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
                                    schema = @Schema(implementation = APIResponse.class))
                    )
            }
    )
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    ResponseEntity<APIResponse<Page<UserDTO>>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );

    @Operation(
            summary     = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Пользователь найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<APIResponse<UserDTO>> getId(@PathVariable Long id);

    @Operation(
            summary     = "Обновить данные пользователя",
            description = "Обновляет информацию пользователя по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Данные пользователя успешно обновлены",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))
                    )
            }
    )
    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<APIResponse<UserDTO>> update(
            @PathVariable(name = "id") Long id,
            @RequestBody UserDTO userDTO
    );

    @Operation(
            summary     = "Удалить пользователя",
            description = "Удаляет пользователя по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Пользователь успешно удалён",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id);

    @Operation(
            summary     = "Получить общий баланс пользователя",
            description = "Возвращает суммарный баланс всех карт пользователя",
            responses   = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Баланс получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))
                    )
            }
    )
    @GetMapping("/{userId}/total-balance")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<APIResponse<TotalCardBalanceDTO>> getTotalBalance(@PathVariable Long userId);

}
