package com.example.bankcards.dto.Requests;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ReplenishRequest {
    private BigDecimal amount;
}
