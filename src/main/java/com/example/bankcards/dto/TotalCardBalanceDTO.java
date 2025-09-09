package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TotalCardBalanceDTO {
    private Long userId;
    private List<CardBalanceDto> cardBalances;
    private BigDecimal totalBalance;
}
