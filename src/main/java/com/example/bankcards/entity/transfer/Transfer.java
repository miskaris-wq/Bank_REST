package com.example.bankcards.entity.transfer;

import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Сущность, представляющая перевод денежных средств между картами.
 * <p>
 * Каждый перевод создаётся пользователем-инициатором и содержит ссылки
 * на карту-отправителя и карту-получателя.
 * </p>
 *
 * <ul>
 *     <li>{@link #initiator} — пользователь, выполнивший перевод;</li>
 *     <li>{@link #fromCard} — карта, с которой списываются средства;</li>
 *     <li>{@link #toCard} — карта, на которую зачисляются средства;</li>
 *     <li>{@link #amount} — сумма перевода (неотрицательная);</li>
 *     <li>{@link #status} — статус перевода (например, PROCESS, COMPLETED);</li>
 *     <li>{@link #createdAt} — время создания записи.</li>
 * </ul>
 *
 * @author ksenya
 */
@Entity
@Table(name = "transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    /** Уникальный идентификатор перевода. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Пользователь, инициировавший перевод. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_user_id", nullable = false)
    private User initiator;

    /** Карта, с которой списываются средства. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_card_id", nullable = false)
    private BankCard fromCard;

    /** Карта, на которую зачисляются средства. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_card_id", nullable = false)
    private BankCard toCard;

    /** Сумма перевода (не может быть отрицательной). */
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal amount;

    /** Статус перевода (PROCESS, COMPLETED, CANCELLED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    /** Дата и время создания перевода. */
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Устанавливает значения по умолчанию перед сохранением:
     * <ul>
     *     <li>{@link #createdAt} = текущее время;</li>
     *     <li>{@link #status} = {@link TransferStatus#PROCESS}, если не указан.</li>
     * </ul>
     */
    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (status == null) {
            status = TransferStatus.PROCESS;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        return id != null && id.equals(((Transfer) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
