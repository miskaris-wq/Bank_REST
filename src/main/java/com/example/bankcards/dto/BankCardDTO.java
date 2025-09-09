package com.example.bankcards.dto;

import com.example.bankcards.entity.bankcard.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankCardDTO {

    private Long ownerId;

    private Long cardId;

    private LocalDate expirationDate;

    private Status status;

    private String cardNumber;

    private BigDecimal balance;
}
