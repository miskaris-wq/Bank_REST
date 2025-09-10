package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.TransferController;
import com.example.bankcards.dto.requests.TransferUserRequest;
import com.example.bankcards.dto.payload.TransferUserDto;
import com.example.bankcards.dto.response.APIResponse;
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
    public ResponseEntity<APIResponse<TransferUserDto>> transfer(Long userId, TransferUserRequest request) {
        TransferUserDto dto = transferServiceImpl.transferFromToCardUser(userId, request);
        APIResponse<TransferUserDto> response =
                new APIResponse<>(dto, "Перевод успешно выполнен", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<Page<TransferUserDto>>> getAll(int page, int size) {
        Page<TransferUserDto> transfers = transferServiceImpl.getAll(page, size);
        APIResponse<Page<TransferUserDto>> response =
                new APIResponse<>(transfers, "Все переводы возвращены", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<Page<TransferUserDto>>> getAllByUser(Long userId, int page, int size) {
        Page<TransferUserDto> transfers = transferServiceImpl.getAllByUser(page, size, userId);
        APIResponse<Page<TransferUserDto>> response =
                new APIResponse<>(transfers, "История переводов пользователя получена", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<TransferUserDto>> getById(Long id) {
        TransferUserDto dto = transferServiceImpl.getById(id);
        APIResponse<TransferUserDto> response =
                new APIResponse<>(dto, "Перевод найден", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
