package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.CardRequestController;
import com.example.bankcards.dto.payload.CardRequestDTO;
import com.example.bankcards.dto.response.APIResponse;
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
    public ResponseEntity<APIResponse<CardRequestDTO>> requestBlock(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.requestBlock(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(APIResponse.ofSuccess(dto, "Заявка на блокировку создана", HttpStatus.ACCEPTED));
    }

    @Override
    public ResponseEntity<APIResponse<CardRequestDTO>> reject(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.requestRejected(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(dto, "Заявка отклонена администратором", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<CardRequestDTO>> getUserRequest(Long id, Long userId) {
        CardRequestDTO dto = cardRequestServiceImpl.getIdByUser(id, userId);
        return ResponseEntity.ok(APIResponse.ofSuccess(dto, "Заявка пользователя получена", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<CardRequestDTO>> getById(Long id) {
        CardRequestDTO dto = cardRequestServiceImpl.getId(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(dto, "Заявка найдена", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<Page<CardRequestDTO>>> getAllByUser(Long userId, int page, int size) {
        Page<CardRequestDTO> requests = cardRequestServiceImpl.getAllByUser(userId, page, size);
        return ResponseEntity.ok(APIResponse.ofSuccess(requests, "Заявки пользователя возвращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<Page<CardRequestDTO>>> getAll(int page, int size) {
        Page<CardRequestDTO> requests = cardRequestServiceImpl.getAll(page, size);
        return ResponseEntity.ok(APIResponse.ofSuccess(requests, "Список всех заявок возвращён", HttpStatus.OK));
    }
}
