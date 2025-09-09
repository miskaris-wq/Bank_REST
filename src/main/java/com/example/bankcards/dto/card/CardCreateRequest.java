package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class CardCreateRequest {
    @NotBlank @Size(min = 12, max = 30)
    private String cardNumber;

    @NotBlank @Size(max = 120)
    private String holder;

    @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}")
    private String expiry;

    private Long ownerId;
}
