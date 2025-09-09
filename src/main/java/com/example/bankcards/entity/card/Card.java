package com.example.bankcards.entity.card;

import com.example.bankcards.entity.crypto.CardNumberAttributeConverter;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Convert(converter = CardNumberAttributeConverter.class)
    @Column(name = "card_number_enc", nullable = false, unique = true, length = 512)
    private String cardNumber;

    @Column(nullable = false, length = 120)
    private String holder;

    @Column(nullable = false, length = 7)
    private String expiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 4)
    private String last4;

    @Version
    private Long version;

    // ====== helpers ======
    @Transient
    public String maskedNumber() {
        return "**** **** **** " + last4;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        if (cardNumber != null && cardNumber.length() >= 4) {
            this.last4 = cardNumber.substring(cardNumber.length() - 4);
        }
    }
}
