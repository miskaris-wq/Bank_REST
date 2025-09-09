package com.example.bankcards.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
@Builder
public class TransferResponse {
    Long id;
    Long fromCardId;
    Long toCardId;
    BigDecimal amount;
    String status; // status: COMPLETED | PROCESS | CANCELLED
    OffsetDateTime createdAt;
}
