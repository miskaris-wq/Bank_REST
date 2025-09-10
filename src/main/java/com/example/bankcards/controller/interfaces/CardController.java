package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card", description = "Методы управления банковскими картами")
@RequestMapping("/api/v1/cards")
public interface CardController {

    @Operation(
            summary = "Список всех карт",
            description = "Возвращает полный список карт банка постранично",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карты получены",
                            content = @Content(schema = @Schema(implementation = CardsResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав для доступа",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<CardsResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Карты текущего пользователя",
            description = "Возвращает список карт, принадлежащих пользователю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карты пользователя найдены",
                            content = @Content(schema = @Schema(implementation = CardsResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PreAuthorize("#userId == authentication.principal.id")
    @GetMapping("/all/by-user/{userId}")
    ResponseEntity<CardsResponse> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Карта по ID",
            description = "Возвращает данные карты по её идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта найдена",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #cardId)")
    @GetMapping("/{cardId}")
    ResponseEntity<CardResponse> getById(@PathVariable Long cardId);

    @Operation(
            summary = "Создать карту",
            description = "Создание новой банковской карты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта успешно создана",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    ResponseEntity<CardResponse> create(@RequestBody CreateCardRequest request);

    @Operation(
            summary = "Заблокировать карту",
            description = "Переводит карту в статус блокировки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта заблокирована",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PatchMapping("/blocked/{id}")
    ResponseEntity<CardResponse> block(@PathVariable Long id);

    @Operation(
            summary = "Активировать карту",
            description = "Снимает блокировку или активирует карту",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта активирована",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Карта уже активна",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PatchMapping("/activate/{id}")
    ResponseEntity<CardResponse> activate(@PathVariable Long id);

    @Operation(
            summary = "Удалить карту",
            description = "Удаляет карту из системы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта удалена",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    ResponseEntity<Response<Void>> delete(@PathVariable Long id);

    @Operation(
            summary = "Баланс карты",
            description = "Возвращает текущий баланс карты по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс получен",
                            content = @Content(schema = @Schema(implementation = BalanceResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет доступа",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PreAuthorize("#userId == authentication.principal.id and @cardService.isOwnerCard(#userId, #cardId)")
    @GetMapping("/balance/{cardId}/user/{userId}")
    ResponseEntity<BalanceResponse> getBalance(@PathVariable Long userId, @PathVariable Long cardId);

    @Operation(
            summary = "Пополнить карту",
            description = "Увеличивает баланс карты на указанную сумму",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс обновлён",
                            content = @Content(schema = @Schema(implementation = BalanceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PreAuthorize("@cardService.isOwnerCard(principal.id, #id)")
    @PostMapping("/replenish/{id}")
    ResponseEntity<BalanceResponse> replenish(@PathVariable Long id, @RequestBody ReplenishRequest request);
}
