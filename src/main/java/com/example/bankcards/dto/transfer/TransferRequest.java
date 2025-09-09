package com.example.bankcards.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class TransferRequest {
    @NotNull private Long fromId;
    @NotNull private Long toId;
    @NotNull @DecimalMin("0.01")
    private BigDecimal amount;
}
