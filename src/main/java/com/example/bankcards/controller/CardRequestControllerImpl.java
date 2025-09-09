package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.CardRequestController;
import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.dto.Responses.CardRequestResponse;
import com.example.bankcards.dto.Responses.CardRequestsResponse;
import com.example.bankcards.service.CardRequestService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardRequestControllerImpl implements CardRequestController {

    private final CardRequestService cardRequestService;

    public CardRequestControllerImpl(CardRequestService cardRequestService) {
        this.cardRequestService = cardRequestService;
    }

    @Override
    public ResponseEntity<CardRequestResponse> requestBlock(Long id) {

        CardRequestDTO cardRequestDTO = cardRequestService.requestBlock(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new CardRequestResponse(cardRequestDTO, "Заявка успешно отправлена. Ждите!", HttpStatus.ACCEPTED));
    }

    @Override
    public ResponseEntity<CardRequestResponse> requestRejected(Long id) {

        CardRequestDTO cardRequestDTO = cardRequestService.requestRejected(id);

        return ResponseEntity.status(HttpStatus.OK).body(new CardRequestResponse(cardRequestDTO, "Запрос был успешно отклонён!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardRequestResponse> getIdByUser(Long id, Long userId) {
        CardRequestDTO cardRequestDTO = cardRequestService.getIdByUser(id,userId);

        return ResponseEntity.status(HttpStatus.OK).body(new CardRequestResponse(cardRequestDTO, "Запрос получен!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardRequestResponse> getById(Long id) {

        CardRequestDTO cardRequestDTO = cardRequestService.getId(id);

        return ResponseEntity.status(HttpStatus.OK).body(new CardRequestResponse(cardRequestDTO, "Запрос получен!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardRequestsResponse> getAllByUser(int pageNumber, int pageSize, Long userId) {

        Page<CardRequestDTO> cardRequests = cardRequestService.getAllByUser(userId, pageNumber, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(new CardRequestsResponse(cardRequests, "Запрос пользователя получен!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardRequestsResponse> getAll(int pageNumber, int pageSize) {
        Page<CardRequestDTO> cardRequests = cardRequestService.getAll(pageNumber, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(new CardRequestsResponse(cardRequests, "Все запросы получены!", HttpStatus.OK));
    }
}
