package com.example.bankcards.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для отображения баланса карты.
 *
 * <p>Используется при возврате информации о состоянии карты
 * (баланс, срок действия, маскированный номер).</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardBalanceDTO {

    /**
     * Уникальный идентификатор карты.
     */
    private Long cardId;

    /**
     * Текущий баланс карты.
     */
    private BigDecimal balance;

    /**
     * Маскированный номер карты (например, **** **** **** 1234).
     */
    private String maskedCardNumber;

    /**
     * Дата окончания действия карты.
     */
    private LocalDate expirationDate;
}
