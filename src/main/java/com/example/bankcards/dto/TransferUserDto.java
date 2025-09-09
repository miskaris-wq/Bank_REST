package com.example.bankcards.dto;

import com.example.bankcards.entity.transfer.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferUserDto {
    private Long transactionId;
    private Long initiatorId;
    private BankCardDTO fromCard;
    private BankCardDTO toCard;
    private BigDecimal amount;
    private TransferStatus status;
    private Instant createdAt;
}