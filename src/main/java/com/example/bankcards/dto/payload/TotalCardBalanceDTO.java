package com.example.bankcards.dto.payload;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для отображения общего баланса пользователя.
 *
 * <p>Содержит список всех карт пользователя с их балансами
 * и суммарный баланс по всем картам.</p>
 */
@Data
@Builder
public class TotalCardBalanceDTO {

    /**
     * Идентификатор пользователя.
     */
    private Long userId;

    /**
     * Список карт пользователя с их балансами.
     */
    private List<CardBalanceDTO> cardBalances;

    /**
     * Общая сумма средств на всех картах пользователя.
     */
    private BigDecimal totalBalance;
}
