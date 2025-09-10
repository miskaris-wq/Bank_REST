package com.example.bankcards.dto.requests;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ReplenishRequest {
    private BigDecimal amount;
}
