package com.example.bankcards.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для передачи информации о заявке на блокировку карты.
 *
 * <p>Используется для отображения и возврата информации о запросах пользователей
 * на блокировку их банковских карт.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDTO {

    /**
     * Уникальный идентификатор заявки.
     */
    private Long requestId;

    /**
     * Идентификатор пользователя, который инициировал заявку.
     */
    private Long initiatorId;

    /**
     * Идентификатор карты, к которой относится заявка.
     */
    private Long cardId;

    /**
     * Статус заявки (например: PENDING, APPROVED, REJECTED).
     */
    private String status;

    /**
     * Дата и время создания заявки.
     */
    private LocalDateTime requestedAt;

    /**
     * Дополнительное сообщение пользователя, связанное с заявкой.
     */
    private String message;
}
