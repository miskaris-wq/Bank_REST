package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.dto.requests.CreateCardRequest;
import com.example.bankcards.dto.requests.ReplenishRequest;
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

@Tag(name = "Card", description = "Методы управления банковскими картами")
@RequestMapping("/api/v1/cards")
public interface CardController {

    @Operation(
            summary = "Список всех карт",
            description = "Возвращает полный список карт банка постранично",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карты получены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав для доступа",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<APIResponse<Page<BankCardDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Карты текущего пользователя",
            description = "Возвращает список карт, принадлежащих пользователю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карты пользователя найдены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PreAuthorize("#userId == authentication.principal.id")
    @GetMapping("/all/by-user/{userId}")
    ResponseEntity<APIResponse<Page<BankCardDTO>>> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "Карта по ID",
            description = "Возвращает данные карты по её идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #cardId)")
    @GetMapping("/{cardId}")
    ResponseEntity<APIResponse<BankCardDTO>> getById(@PathVariable Long cardId);

    @Operation(
            summary = "Создать карту",
            description = "Создание новой банковской карты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта успешно создана",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    ResponseEntity<APIResponse<BankCardDTO>> create(@RequestBody CreateCardRequest request);

    @Operation(
            summary = "Заблокировать карту",
            description = "Переводит карту в статус блокировки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта заблокирована",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PatchMapping("/blocked/{id}")
    ResponseEntity<APIResponse<BankCardDTO>> block(@PathVariable Long id);

    @Operation(
            summary = "Активировать карту",
            description = "Снимает блокировку или активирует карту",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта активирована",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Карта уже активна",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @PatchMapping("/activate/{id}")
    ResponseEntity<APIResponse<BankCardDTO>> activate(@PathVariable Long id);

    @Operation(
            summary = "Удалить карту",
            description = "Удаляет карту из системы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта удалена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id);

    @Operation(
            summary = "Баланс карты",
            description = "Возвращает текущий баланс карты по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Нет доступа",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PreAuthorize("#userId == authentication.principal.id and @cardService.isOwnerCard(#userId, #cardId)")
    @GetMapping("/balance/{cardId}/user/{userId}")
    ResponseEntity<APIResponse<CardBalanceDTO>> getBalance(@PathVariable Long userId, @PathVariable Long cardId);

    @Operation(
            summary = "Пополнить карту",
            description = "Увеличивает баланс карты на указанную сумму",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс обновлён",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIResponse.class)))
            }
    )
    @PreAuthorize("@cardService.isOwnerCard(principal.id, #id)")
    @PostMapping("/replenish/{id}")
    ResponseEntity<APIResponse<CardBalanceDTO>> replenish(@PathVariable Long id, @RequestBody ReplenishRequest request);
}
