package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.*;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    @Override
    public ResponseEntity<CardsResponse> getAll(int page, int size) {
        Page<BankCardDTO> result = cardService.getAll(page, size);
        CardsResponse response = new CardsResponse(result, "Все карты успешно возвращены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardsResponse> getAllByUser(Long userId, int page, int size) {
        Page<BankCardDTO> cards = cardService.getAllCurrentUser(page, size, userId);
        CardsResponse response = new CardsResponse(cards, "Карты пользователя получены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> getById(Long cardId) {
        BankCardDTO card = cardService.getById(cardId);
        CardResponse response = new CardResponse(card, "Данные по карте найдены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> create(CreateCardRequest request) {
        BankCardDTO newCard = cardService.create(request);
        CardResponse response = new CardResponse(newCard, "Карта успешно создана", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> block(Long id) {
        BankCardDTO card = cardService.blocked(id);
        CardResponse response = new CardResponse(card, "Карта заблокирована", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> activate(Long id) {
        BankCardDTO card = cardService.activate(id);
        CardResponse response = new CardResponse(card, "Карта активирована", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Response<Void>> delete(Long id) {
        cardService.delete(id);
        Response<Void> response = Response.of("Карта удалена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BalanceResponse> getBalance(Long userId, Long cardId) {
        CardBalanceDto balance = cardService.getBalance(userId, cardId);
        BalanceResponse response = new BalanceResponse(balance, "Баланс карты получен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BalanceResponse> replenish(Long id, ReplenishRequest request) {
        CardBalanceDto updatedBalance = cardService.deposit(id, request.getAmount());
        BalanceResponse response = new BalanceResponse(updatedBalance, "Баланс успешно пополнен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
