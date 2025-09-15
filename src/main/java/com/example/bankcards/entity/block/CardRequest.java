package com.example.bankcards.entity.block;

import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность {@code CardRequest} представляет заявку пользователя на блокировку банковской карты.
 * <p>
 * Заявка создается владельцем карты и должна быть рассмотрена администратором.
 * До одобрения или отклонения заявки карта остаётся в исходном состоянии.
 * </p>
 *
 * @author ksenya
 */
@Entity
@Table(name = "block_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    /**
     * Уникальный идентификатор заявки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, создавший заявку (инициатор).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    /**
     * Карта, к которой относится заявка.
     * <p>
     * Каждая карта может иметь только одну активную заявку на блокировку.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private BankCard card;

    /**
     * Текущий статус заявки.
     * Может быть {@link CardRequestStatus#PENDING}, {@link CardRequestStatus#APPROVED} или {@link CardRequestStatus#REJECTED}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardRequestStatus status;

    /**
     * Дата и время создания заявки.
     */
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    /**
     * Дополнительное сообщение, указанное инициатором при создании заявки.
     */
    @Column(name = "message", length = 500)
    private String message;

    /**
     * Устанавливает значения по умолчанию перед сохранением:
     * <ul>
     *     <li>{@link #requestedAt} — текущее время, если не указано;</li>
     *     <li>{@link #status} — {@link CardRequestStatus#PENDING}, если не указан.</li>
     * </ul>
     */
    @PrePersist
    public void prePersist() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = CardRequestStatus.PENDING;
        }
    }

    /**
     * Два объекта {@code CardRequest} считаются равными,
     * если совпадают их {@link #id}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardRequest)) return false;
        return id != null && id.equals(((CardRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
