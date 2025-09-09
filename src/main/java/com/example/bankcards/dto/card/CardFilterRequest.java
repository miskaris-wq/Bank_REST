package com.example.bankcards.dto.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class CardFilterRequest {
    private String status;
    private String last4;  // "1234"
}
