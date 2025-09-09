package com.example.bankcards.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferUserRequest {
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}
