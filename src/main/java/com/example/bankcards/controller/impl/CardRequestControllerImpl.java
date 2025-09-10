package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.CardRequestController;
import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.dto.Responses.CardRequestResponse;
import com.example.bankcards.dto.Responses.CardRequestsResponse;
import com.example.bankcards.service.impl.CardRequestServiceImpl;
import com.example.bankcards.service.interfaces.CardRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardRequestControllerImpl implements CardRequestController {

    private final CardRequestService cardRequestServiceImpl;

    @Override
    public ResponseEntity<CardRequestResponse> requestBlock(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.requestBlock(id);
        CardRequestResponse response = new CardRequestResponse(dto, "Заявка на блокировку создана", HttpStatus.ACCEPTED);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<CardRequestResponse> reject(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.requestRejected(id);
        CardRequestResponse response = new CardRequestResponse(dto, "Заявка отклонена администратором", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardRequestResponse> getUserRequest(Long id, Long userId) {
        CardRequestDTO dto = cardRequestServiceImpl.getIdByUser(id, userId);
        CardRequestResponse response = new CardRequestResponse(dto, "Заявка пользователя получена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardRequestResponse> getById(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.getId(id);
        CardRequestResponse response = new CardRequestResponse(dto, "Заявка найдена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardRequestsResponse> getAllByUser(Long userId, int page, int size) {
        Page<CardRequestDTO> requests = cardRequestServiceImpl.getAllByUser(userId, page, size);
        CardRequestsResponse response = new CardRequestsResponse(requests, "Заявки пользователя возвращены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CardRequestsResponse> getAll(int page, int size) {
        Page<CardRequestDTO> requests = cardRequestServiceImpl.getAll(page, size);
        CardRequestsResponse response = new CardRequestsResponse(requests, "Список всех заявок возвращён", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
