package com.example.bankcards.dto.requests;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * DTO-запрос для пополнения баланса карты.
 * <p>
 * Используется в методе {@code /api/v1/cards/replenish/{id}}.
 * Содержит сумму, на которую необходимо увеличить баланс карты.
 * </p>
 *
 * @author ksenya
 */
@Getter
@Builder
public class ReplenishRequest {

    /**
     * Сумма пополнения карты.
     * Должна быть положительной и не превышать максимально допустимое значение для BigDecimal.
     */
    private BigDecimal amount;
}
