package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.payload.TransferUserDto;
import com.example.bankcards.dto.requests.TransferUserRequest;
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

@RequestMapping("/api/v1/transfer")
@Tag(name = "Transfers", description = "Переводы между картами и просмотр истории переводов")
public interface TransferController {

    @Operation(
            summary = "Выполнить перевод",
            description = "Переводит средства между картами текущего пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Перевод выполнен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос: одинаковые карты или недостаточно средств",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав на выполнение операции",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Одна из карт не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Карты должны быть активны",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PostMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id and #request.toCardId != #request.fromCardId")
    ResponseEntity<APIResponse<TransferUserDto>> transfer(
            @PathVariable Long userId,
            @RequestBody TransferUserRequest request
    );

    @Operation(
            summary = "Все переводы (администратор)",
            description = "Администратор получает список всех переводов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список переводов получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<APIResponse<Page<TransferUserDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Переводы пользователя",
            description = "Возвращает все переводы, выполненные конкретным пользователем",
            responses = {
                    @ApiResponse(responseCode = "200", description = "История переводов получена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Попытка доступа к чужой истории переводов",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<APIResponse<Page<TransferUserDto>>> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Получить перевод по ID",
            description = "Возвращает детали перевода по его идентификатору (только администратор)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные перевода найдены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Перевод не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    ResponseEntity<APIResponse<TransferUserDto>> getById(@PathVariable Long id);
}
