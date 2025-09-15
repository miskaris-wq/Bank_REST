package com.example.bankcards.dto.payload;

import com.example.bankcards.entity.transfer.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO для передачи информации о переводе между картами.
 *
 * <p>Используется для отображения истории переводов и их деталей.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferUserDto {

    /**
     * Уникальный идентификатор транзакции перевода.
     */
    private Long transactionId;

    /**
     * Идентификатор пользователя, который инициировал перевод.
     */
    private Long initiatorId;

    /**
     * Карта, с которой был произведён перевод.
     */
    private BankCardDTO fromCard;

    /**
     * Карта, на которую был произведён перевод.
     */
    private BankCardDTO toCard;

    /**
     * Сумма перевода.
     */
    private BigDecimal amount;

    /**
     * Статус перевода (например: PROCESS, COMPLETED, CANCELLED).
     */
    private TransferStatus status;

    /**
     * Дата и время создания перевода.
     */
    private Instant createdAt;
}
