package com.example.bankcards.mapper;

import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.transfer.CardTransfer;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public TransferResponse toResponse(CardTransfer t) {
        if (t == null) return null;
        return TransferResponse.builder()
                .id(t.getId())
                .fromCardId(t.getFromCard().getId())
                .toCardId(t.getToCard().getId())
                .amount(t.getAmount())
                .status(t.getStatus().name())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
