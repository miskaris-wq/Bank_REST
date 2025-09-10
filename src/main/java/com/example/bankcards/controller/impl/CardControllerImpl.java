package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.*;
import com.example.bankcards.service.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {

    private final CardService cardServiceImpl;

    @Override
    public ResponseEntity<CardsResponse> getAll(int page, int size) {
        Page<BankCardDTO> result = cardServiceImpl.getAll(page, size);
        CardsResponse response = new CardsResponse(result, "Все карты успешно возвращены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardsResponse> getAllByUser(Long userId, int page, int size) {
        Page<BankCardDTO> cards = cardServiceImpl.getAllCurrentUser(page, size, userId);
        CardsResponse response = new CardsResponse(cards, "Карты пользователя получены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> getById(Long cardId) {
        BankCardDTO card = cardServiceImpl.getById(cardId);
        CardResponse response = new CardResponse(card, "Данные по карте найдены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> create(CreateCardRequest request) {
        BankCardDTO newCard = cardServiceImpl.create(request);
        CardResponse response = new CardResponse(newCard, "Карта успешно создана", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> block(Long id) {
        BankCardDTO card = cardServiceImpl.blocked(id);
        CardResponse response = new CardResponse(card, "Карта заблокирована", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardResponse> activate(Long id) {
        BankCardDTO card = cardServiceImpl.activate(id);
        CardResponse response = new CardResponse(card, "Карта активирована", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Response<Void>> delete(Long id) {
        cardServiceImpl.delete(id);
        Response<Void> response = Response.of("Карта удалена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BalanceResponse> getBalance(Long userId, Long cardId) {
        CardBalanceDTO balance = cardServiceImpl.getBalance(userId, cardId);
        BalanceResponse response = new BalanceResponse(balance, "Баланс карты получен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BalanceResponse> replenish(Long id, ReplenishRequest request) {
        CardBalanceDTO updatedBalance = cardServiceImpl.deposit(id, request.getAmount());
        BalanceResponse response = new BalanceResponse(updatedBalance, "Баланс успешно пополнен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
