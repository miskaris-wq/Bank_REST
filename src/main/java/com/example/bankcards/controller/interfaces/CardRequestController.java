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

@RequestMapping("/api/v1/card-request")
@Tag(name = "Card Requests", description = "Операции с запросами к картам")
public interface CardRequestController {

    @Operation(
            summary     = "Запросить блокировку карты",
            description = "Пользователь создаёт заявку на блокировку своей банковской карты. " +
                    "После успешного создания заявки карта продолжает работать до её обработки администратором.",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description  = "Заявка на блокировку принята и ожидает обработки",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestResponse.class)
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
                    )
            }
    )

    @PostMapping("/block/{id}")
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #id)")
    ResponseEntity<CardRequestResponse> requestBlock(
            @PathVariable Long id
    );

    @Operation(
            summary     = "Отклонить заявку на блокировку карты",
            description = "Администратор отклоняет ранее созданную заявку на блокировку карты пользователя.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Заявка успешно отклонена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: требуется роль администратора",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Заявка с указанным `id` не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/rejected/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardRequestResponse> requestRejected(@PathVariable Long id);

    @Operation(
            summary     = "Получить заявку на блокировку по ID",
            description = "Возвращает детали ранее созданной заявки на блокировку карты по её идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Запрос успешно найден",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: пользователь не владеет заявкой",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Заявка с указанным `id` не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}/by-user")
    @PreAuthorize("#userId == authentication.principal.id and @cardRequestService.getIsOwnerCardRequest(#id, authentication.principal.id)")
    ResponseEntity<CardRequestResponse> getIdByUser(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "userId") Long userId
    );

    @Operation(
            summary     = "Получить заявку на блокировку по ID (администратор)",
            description = "Администратор получает детали ранее созданной заявки на блокировку карты по её идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Заявка успешно найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: требуется роль администратора",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Заявка с указанным `id` не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardRequestResponse> getById(
            @PathVariable(name = "id") Long id
    );

    @Operation(
            summary     = "Получить все заявки пользователя",
            description = "Возвращает список заявок на блокировку карт, созданных данным пользователем.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список заявок успешно возвращён",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: попытка получить заявки другого пользователя",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/all/by-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<CardRequestsResponse> getAllByUser(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @PathVariable(name = "userId") Long userId
    );



    @Operation(
            summary     = "Получить все заявки (для администратора)",
            description = "Администратор получает полный список всех заявок на блокировку карт всех пользователей.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список всех заявок успешно возвращён",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardRequestsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: требуется роль администратора",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    ResponseEntity<CardRequestsResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );
}