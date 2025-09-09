package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.TransferResponse;
import com.example.bankcards.dto.Responses.TransfersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/transfer")
@Tag(name = "Transfer",     description = "Выполнение переводов между банковскими картами пользователей и просмотр истории переводов")
public interface TransferController {

    @Operation(
            summary     = "Перевести средства между картами пользователя",
            description = "Выполняет перевод указанной суммы с одной карты текущего пользователя на другую",
            responses    = {
                    @ApiResponse(
                            responseCode = "201",
                            description  = "Перевод выполнен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = TransferResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description  = "Неверный запрос: одинаковые карты или недостаточно средств",
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Одна из карт не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
                    ,
                    @ApiResponse(
                            responseCode = "409",
                            description  = "Обе карты должны быть активированы",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id and #request.toCardId != #request.fromCardId")
    ResponseEntity<TransferResponse> transferFromToCardUser(
            @PathVariable Long userId,
            @RequestBody TransferUserRequest request
    );

    @Operation(
            summary     = "Получить все переводы",
            description = "Возвращает страницу списка всех переводов",
            responses   = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список переводов получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = TransfersResponse.class)
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
    ResponseEntity<TransfersResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );


    @Operation(
            summary     = "Получить все переводы пользователя",
            description = "Возвращает страницу списка всех переводов, выполненных данным пользователем.",
            responses   = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список переводов успешно получен",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = TransfersResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: попытка получить переводы другого пользователя",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<TransfersResponse> getAllBylUser(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @PathVariable("userId") Long userId
    );

    @Operation(
            summary     = "Получить перевод по ID",
            description = "Возвращает информацию о переводе по его уникальному идентификатору (доступно только администратору)",
            responses   = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Данные перевода получены успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = TransferResponse.class)
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
                            description  = "Перевод с указанным ID не найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<TransferResponse> getById(@PathVariable Long id);

}
