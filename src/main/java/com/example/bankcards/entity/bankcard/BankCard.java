package com.example.bankcards.entity.bankcard;

import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность банковской карты.
 * <p>
 * Хранит информацию о номере карты, владельце,
 * сроке действия, статусе и текущем балансе.
 * </p>
 *
 * <ul>
 *     <li>По умолчанию при создании:
 *         <ul>
 *             <li>{@link Status#ACTIVE} — статус карты;</li>
 *             <li>+5 лет от текущей даты — срок действия;</li>
 *             <li>{@link BigDecimal#ZERO} — начальный баланс.</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author ksenya
 */
@Entity
@Table(name = "bank_card")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCard {

    /**
     * Уникальный идентификатор карты.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Номер карты в формате XXXX XXXX XXXX XXXX (последние 4 цифры видны).
     * Хранится замаскированным.
     */
    @Pattern(
            regexp = "^\\*{4} \\*{4} \\*{4} \\d{4}$",
            message = "Номер карты должен быть в формате XXXX XXXX XXXX XXXX"
    )
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    /**
     * Владелец карты (пользователь).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Дата окончания срока действия карты.
     * Должна быть в будущем.
     */
    @Column(name = "expiration_date", nullable = false)
    @Future(message = "Срок действия карты должен быть в будущем")
    private LocalDate expirationDate;

    /**
     * Текущий статус карты (ACTIVE, BLOCKED, EXPIRED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Баланс карты.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    /**
     * Метод обратного вызова JPA.
     * Устанавливает значения по умолчанию при первом сохранении.
     */
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.ACTIVE;
        }
        if (expirationDate == null) {
            expirationDate = LocalDate.now().plusYears(5);
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}
