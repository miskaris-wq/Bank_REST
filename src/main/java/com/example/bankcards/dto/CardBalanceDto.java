package com.example.bankcards.dto;

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
public class CardBalanceDto {
    private Long cardId;
    private BigDecimal balance;
    private String maskedCardNumber;
    private LocalDate expirationDate;
}