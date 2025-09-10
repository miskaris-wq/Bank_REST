package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.payload.CardRequestDTO;
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

@Tag(name = "Card Requests", description = "Заявки на блокировку банковских карт")
@RequestMapping("/api/v1/card-request")
public interface CardRequestController {

    @Operation(
            summary = "Создать заявку на блокировку",
            description = "Владелец карты отправляет запрос на её блокировку. Пока администратор не рассмотрит заявку, карта остаётся активной.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Заявка принята",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не является владельцем карты",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PostMapping("/block/{id}")
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #id)")
    ResponseEntity<APIResponse<CardRequestDTO>> requestBlock(@PathVariable Long id);

    @Operation(
            summary = "Отклонить заявку",
            description = "Администратор отклоняет заявку на блокировку карты.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка отклонена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PostMapping("/rejected/{id}")
    ResponseEntity<APIResponse<CardRequestDTO>> reject(@PathVariable Long id);

    @Operation(
            summary = "Получить заявку пользователя по ID",
            description = "Возвращает заявку на блокировку карты, если она принадлежит текущему пользователю.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Доступ к чужой заявке запрещён",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @GetMapping("/{id}/by-user")
    @PreAuthorize("#userId == authentication.principal.id and @cardRequestService.getIsOwnerCardRequest(#id, authentication.principal.id)")
    ResponseEntity<APIResponse<CardRequestDTO>> getUserRequest(@PathVariable Long id, @RequestParam Long userId);

    @Operation(
            summary = "Получить заявку по ID (для администратора)",
            description = "Администратор может просмотреть любую заявку по её идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    ResponseEntity<APIResponse<CardRequestDTO>> getById(@PathVariable Long id);

    @Operation(
            summary = "Заявки пользователя",
            description = "Возвращает все заявки на блокировку, созданные конкретным пользователем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявки пользователя найдены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Попытка получить чужие заявки",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @GetMapping("/all/by-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<APIResponse<Page<CardRequestDTO>>> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Все заявки (администратор)",
            description = "Администратор получает список всех заявок в системе.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Все заявки возвращены",
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
    ResponseEntity<APIResponse<Page<CardRequestDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );
}
