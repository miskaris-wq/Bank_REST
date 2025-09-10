package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Responses.CardRequestResponse;
import com.example.bankcards.dto.Responses.CardRequestsResponse;
import com.example.bankcards.dto.Responses.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
                            content = @Content(schema = @Schema(implementation = CardRequestResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не является владельцем карты",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PostMapping("/block/{id}")
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #id)")
    ResponseEntity<CardRequestResponse> requestBlock(@PathVariable Long id);

    @Operation(
            summary = "Отклонить заявку",
            description = "Администратор отклоняет заявку на блокировку карты.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка отклонена",
                            content = @Content(schema = @Schema(implementation = CardRequestResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PostMapping("/rejected/{id}")
    ResponseEntity<CardRequestResponse> reject(@PathVariable Long id);

    @Operation(
            summary = "Получить заявку пользователя по ID",
            description = "Возвращает заявку на блокировку карты, если она принадлежит текущему пользователю.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка найдена",
                            content = @Content(schema = @Schema(implementation = CardRequestResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Доступ к чужой заявке запрещён",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @GetMapping("/{id}/by-user")
    @PreAuthorize("#userId == authentication.principal.id and @cardRequestService.getIsOwnerCardRequest(#id, authentication.principal.id)")
    ResponseEntity<CardRequestResponse> getUserRequest(@PathVariable Long id, @RequestParam Long userId);

    @Operation(
            summary = "Получить заявку по ID (для администратора)",
            description = "Администратор может просмотреть любую заявку по её идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка найдена",
                            content = @Content(schema = @Schema(implementation = CardRequestResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Заявка не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    ResponseEntity<CardRequestResponse> getById(@PathVariable Long id);

    @Operation(
            summary = "Заявки пользователя",
            description = "Возвращает все заявки на блокировку, созданные конкретным пользователем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявки пользователя найдены",
                            content = @Content(schema = @Schema(implementation = CardRequestsResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Попытка получить чужие заявки",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @GetMapping("/all/by-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<CardRequestsResponse> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Все заявки (администратор)",
            description = "Администратор получает список всех заявок в системе.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Все заявки возвращены",
                            content = @Content(schema = @Schema(implementation = CardRequestsResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав администратора",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<CardRequestsResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );
}
