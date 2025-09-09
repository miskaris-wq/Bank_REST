package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.card.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardResponse toResponse(Card c) {
        if (c == null) return null;
        return CardResponse.builder()
                .id(c.getId())
                .maskedNumber(c.maskedNumber())
                .holder(c.getHolder())
                .expiry(c.getExpiry())
                .status(c.getStatus().name())
                .last4(c.getLast4())
                .balance(c.getBalance())
                .build();
    }
}
