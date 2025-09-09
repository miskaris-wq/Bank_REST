package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.BalanceResponse;
import com.example.bankcards.dto.Responses.CardResponse;
import com.example.bankcards.dto.Responses.CardsResponse;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    @Override
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardsResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        Page<BankCardDTO> cards = cardService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(new CardsResponse(cards, "Все банковские карты успешно возвращены", HttpStatus.OK));
    }

    @Override
    @GetMapping("/all/by-user/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<CardsResponse> getAllCurrentUser(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        Page<BankCardDTO> bankCards = cardService.getAllCurrentUser(pageNumber, pageSize, id);
        return ResponseEntity.ok().body(new CardsResponse(bankCards, "Ваши банковские карты успешно получены!", HttpStatus.OK));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("@cardService.isOwnerCard(authentication.principal.id, #id)")
    public ResponseEntity<CardResponse> getById(@PathVariable Long id) {
        BankCardDTO bankCardDTO = cardService.getById(id);
        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская карточка получена!", HttpStatus.OK));
    }

    @Override
    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> create(@RequestBody CreateCardRequest request) {
        BankCardDTO bankCardDTO = cardService.create(request);
        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская карточка успешно создана!", HttpStatus.OK));
    }

    @Override
    @PatchMapping("/blocked/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> blocked(@PathVariable Long id) {
        BankCardDTO bankCardDTO = cardService.blocked(id);
        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская карточка успешно заблокирована!", HttpStatus.OK));
    }

    @Override
    @PatchMapping("/activate/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> activate(@PathVariable Long id) {
        BankCardDTO bankCardDTO = cardService.activate(id);
        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская карточка успешно активирована!", HttpStatus.OK));
    }

    @Override
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.ok().body(Response.of("Банковская карточка успешно удалена!", HttpStatus.OK));
    }

    @Override
    @GetMapping("/balance/{cardId}/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id and @cardService.isOwnerCard(#userId, #cardId)")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable Long userId,
            @PathVariable Long cardId) {
        CardBalanceDto balance = cardService.getBalance(userId, cardId);
        return ResponseEntity.ok().body(new BalanceResponse(balance, "Баланс карточки успешно получен!", HttpStatus.OK));
    }

    @Override
    @PostMapping("/replenish/{id}")
    @PreAuthorize("@cardService.isOwnerCard(principal.id, #id)")
    public ResponseEntity<BalanceResponse> replenish(@PathVariable Long id, @RequestBody ReplenishRequest request) {
        CardBalanceDto balance = cardService.deposit(id, request.getAmount());
        return ResponseEntity.ok().body(new BalanceResponse(balance, "Банковская карта пополнена!", HttpStatus.OK));
    }
}