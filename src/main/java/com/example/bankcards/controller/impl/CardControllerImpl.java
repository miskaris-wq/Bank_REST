package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.dto.requests.CreateCardRequest;
import com.example.bankcards.dto.requests.ReplenishRequest;
import com.example.bankcards.dto.response.APIResponse;
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
    public ResponseEntity<APIResponse<Page<BankCardDTO>>> getAll(int page, int size) {
        Page<BankCardDTO> result = cardServiceImpl.getAll(page, size);
        return ResponseEntity.ok(APIResponse.ofSuccess(result, "Все карты успешно возвращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<Page<BankCardDTO>>> getAllByUser(Long userId, int page, int size) {
        Page<BankCardDTO> cards = cardServiceImpl.getAllCurrentUser(page, size, userId);
        return ResponseEntity.ok(APIResponse.ofSuccess(cards, "Карты пользователя получены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<BankCardDTO>> getById(Long cardId) {
        BankCardDTO card = cardServiceImpl.getById(cardId);
        return ResponseEntity.ok(APIResponse.ofSuccess(card, "Данные по карте найдены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<BankCardDTO>> create(CreateCardRequest request) {
        BankCardDTO newCard = cardServiceImpl.create(request);
        return ResponseEntity.ok(APIResponse.ofSuccess(newCard, "Карта успешно создана", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<BankCardDTO>> block(Long id) {
        BankCardDTO card = cardServiceImpl.blocked(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(card, "Карта заблокирована", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<BankCardDTO>> activate(Long id) {
        BankCardDTO card = cardServiceImpl.activate(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(card, "Карта активирована", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        cardServiceImpl.delete(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(null, "Карта удалена", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<CardBalanceDTO>> getBalance(Long userId, Long cardId) {
        CardBalanceDTO balance = cardServiceImpl.getBalance(userId, cardId);
        return ResponseEntity.ok(APIResponse.ofSuccess(balance, "Баланс карты получен", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<CardBalanceDTO>> replenish(Long id, ReplenishRequest request) {
        CardBalanceDTO updatedBalance = cardServiceImpl.deposit(id, request.getAmount());
        return ResponseEntity.ok(APIResponse.ofSuccess(updatedBalance, "Баланс успешно пополнен", HttpStatus.OK));
    }
}
