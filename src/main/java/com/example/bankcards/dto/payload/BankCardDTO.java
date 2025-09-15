package com.example.bankcards.dto.payload;

import com.example.bankcards.entity.bankcard.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для передачи данных о банковской карте.
 *
 * <p>Используется для возврата информации о карте в контроллерах
 * и сервисах без необходимости передавать саму сущность {@link com.example.bankcards.entity.bankcard.BankCard}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankCardDTO {

    /**
     * Идентификатор владельца карты (пользователя).
     */
    private Long ownerId;

    /**
     * Уникальный идентификатор карты.
     */
    private Long cardId;

    /**
     * Дата окончания действия карты.
     */
    private LocalDate expirationDate;

    /**
     * Текущий статус карты (ACTIVE, BLOCKED, EXPIRED).
     */
    private Status status;

    /**
     * Номер карты (как правило, замаскированный).
     */
    private String cardNumber;

    /**
     * Баланс карты.
     */
    private BigDecimal balance;
}
