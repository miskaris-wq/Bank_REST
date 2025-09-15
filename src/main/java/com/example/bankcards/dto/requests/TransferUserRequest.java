package com.example.bankcards.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO-запрос для перевода средств между картами.
 * <p>
 * Используется в методе {@code /api/v1/transfer/user/{userId}}.
 * Содержит идентификаторы карт-отправителя и получателя, а также сумму перевода.
 * </p>
 *
 * @author ksenya
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferUserRequest {

    /**
     * ID карты, с которой списываются средства.
     */
    private Long fromCardId;

    /**
     * ID карты, на которую зачисляются средства.
     */
    private Long toCardId;

    /**
     * Сумма перевода.
     * Должна быть положительной и не превышать доступный баланс карты-отправителя.
     */
    private BigDecimal amount;
}
