package com.example.bankcards.entity.block;

import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "card_block_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardBlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(optional = false) @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BlockRequestStatus status;

    @Column(length = 255)
    private String comment; // причина (опционально)

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (status == null) status = BlockRequestStatus.PENDING;
    }
}

