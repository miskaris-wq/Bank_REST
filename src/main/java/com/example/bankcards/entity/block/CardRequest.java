package com.example.bankcards.entity.block;

import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "block_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private BankCard card;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardRequestStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "message", length = 500)
    private String message;
}
