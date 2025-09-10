package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.TransferController;
import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.Responses.TransferResponse;
import com.example.bankcards.dto.Responses.TransfersResponse;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.service.impl.TransferServiceImpl;
import com.example.bankcards.service.interfaces.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {

    private final TransferService transferServiceImpl;

    @Override
    public ResponseEntity<TransferResponse> transfer(Long userId, TransferUserRequest request) {
        TransferUserDto dto = transferServiceImpl.transferFromToCardUser(userId, request);
        TransferResponse response = new TransferResponse(dto, "Перевод успешно выполнен", HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<TransfersResponse> getAll(int page, int size) {
        Page<TransferUserDto> transfers = transferServiceImpl.getAll(page, size);
        TransfersResponse response = new TransfersResponse(transfers, "Все переводы возвращены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransfersResponse> getAllByUser(Long userId, int page, int size) {
        Page<TransferUserDto> transfers = transferServiceImpl.getAllByUser(page, size, userId);
        TransfersResponse response = new TransfersResponse(transfers, "История переводов пользователя получена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransferResponse> getById(Long id) {
        TransferUserDto dto = transferServiceImpl.getById(id);
        TransferResponse response = new TransferResponse(dto, "Перевод найден", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
