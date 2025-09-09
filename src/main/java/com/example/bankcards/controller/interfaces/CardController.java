package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.BalanceResponse;
import com.example.bankcards.dto.Responses.CardResponse;
import com.example.bankcards.dto.Responses.CardsResponse;
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


@Tag(name = "Card", description = "Операции с картами банка")
public interface CardController {

    @Operation(
            summary     = "Получить все карты",
            description = "Возвращает список всех банковских карт",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список карт получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
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
    ResponseEntity<CardsResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );

    @Operation(
            summary     = "Получить все карты текущего пользователя",
            description = "Возвращает список банковских карт, принадлежащих указанному пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список карт получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
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
    @GetMapping("/all/by-user/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    ResponseEntity<CardsResponse> getAllCurrentUser(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );

    @Operation(
            summary     = "Получить карту по её идентификатору",
            description = "Возвращает информацию по банковской карте по переданному `id`. ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта получена успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
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
                            description  = "Карта с указанным `id` не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #id)")
    ResponseEntity<CardResponse> getById(
            @PathVariable Long id
    );

    @Operation(
            summary     = "Создать новую карту",
            description = "Принимает параметры карты и создаёт новую запись",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта создана успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description  = "Неверные или неполные параметры запроса",
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
    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardResponse> create(@RequestBody CreateCardRequest request);

    @Operation(
            summary     = "Заблокировать карту",
            description = "Блокирует карту по её идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно заблокирована",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
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
    @PatchMapping("/blocked/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardResponse> blocked(@PathVariable Long id);

    @Operation(
            summary     = "Активировать карту",
            description = "Активирует ранее созданную или заблокированную карту",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно активирована",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
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
                            responseCode = "409",
                            description  = "Такой статус уже установлен",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PatchMapping("/activate/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardResponse> activate(@PathVariable Long id);

    @Operation(
            summary     = "Удалить карту",
            description = "Удаляет карту по её идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно удалена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
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
                            responseCode = "409",
                            description  = "Такой статус уже установлен",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<Response<Void>> delete(@PathVariable Long id);

    @Operation(
            summary     = "Получить баланс карты",
            description = "Возвращает текущий баланс указанной карты пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Баланс карты получен",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = BalanceResponse.class)
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
                            description  = "Карта не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/balance/{cardId}/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id and @cardService.isOwnerCard(#userId, #cardId)")
    ResponseEntity<BalanceResponse> getBalance(
            @PathVariable Long userId,
            @PathVariable Long cardId
    );

    @Operation(
            summary     = "Пополнить баланс карты",
            description = "Позволяет владельцу банковской карты пополнить её баланс на указанную сумму.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Сумма для пополнения",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ReplenishRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Баланс успешно обновлён",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = BalanceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: пользователь не владеет картой",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным `id` не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description  = "Карта должна активирована",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/replenish/{id}")
    @PreAuthorize("@cardService.isOwnerCard(principal.id, #id)")
    ResponseEntity<BalanceResponse> replenish(@PathVariable Long id, @RequestBody ReplenishRequest request);

}