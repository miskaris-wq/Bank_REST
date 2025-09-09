package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.TransferController;
import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.Responses.TransferResponse;
import com.example.bankcards.dto.Responses.TransfersResponse;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;

    public TransferControllerImpl(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public ResponseEntity<TransferResponse> transferFromToCardUser(Long userId, TransferUserRequest request) {

        TransferUserDto transfer = transferService.transferFromToCardUser(userId, request);

        return ResponseEntity.ok().body(new TransferResponse(transfer, "Перевод успешно завершён", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<TransfersResponse> getAll(int pageNumber, int pageSize) {

        Page<TransferUserDto> transfers = transferService.getAll(pageNumber, pageSize);

        return ResponseEntity.ok().body(new TransfersResponse(transfers, "Перевод успешно завершён", HttpStatus.OK));

    }

    @Override
    public ResponseEntity<TransfersResponse> getAllBylUser(int pageNumber, int pageSize, Long userId) {
        Page<TransferUserDto> transfers = transferService.getAllByUser(pageNumber, pageSize, userId);

        return ResponseEntity.ok().body(new TransfersResponse(transfers, "Все карты пользователя получены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<TransferResponse> getById(Long id) {

        TransferUserDto transfer = transferService.getById(id);

        return ResponseEntity.ok().body(new TransferResponse(transfer, "Перевод успешно получен", HttpStatus.OK));

    }
}
