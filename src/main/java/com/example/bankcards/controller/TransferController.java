package com.example.bankcards.controller;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.transfer.CardTransfer;
import com.example.bankcards.mapper.TransferMapper;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final UserService userService;
    private final TransferMapper transferMapper;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest req,
                                                     Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        var t = transferService.transfer(userId, req);
        return ResponseEntity.ok(transferMapper.toResponse(t));
    }

    // опционально — история переводов пользователя
    @GetMapping
    public Page<TransferResponse> history(@ParameterObject Pageable pageable, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        Page<CardTransfer> page = transferService.historyForUser(userId, pageable);
        return page.map(transferMapper::toResponse);
    }
}
