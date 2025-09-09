package com.example.bankcards.entity.transfer;

import com.example.bankcards.entity.card.Card;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "card_transfers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // обе карты принадлежат одному владельцу (проверяется в сервисе)
    @ManyToOne(optional = false) @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    @ManyToOne(optional = false) @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferStatus status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}

