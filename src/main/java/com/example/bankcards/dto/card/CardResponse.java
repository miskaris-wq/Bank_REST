package com.example.bankcards.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
@Builder
public class CardResponse {
    Long id;
    String maskedNumber;
    String holder;
    String expiry;
    String status;
    String last4;
    BigDecimal balance;
}
