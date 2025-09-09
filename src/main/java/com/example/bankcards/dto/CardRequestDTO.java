package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDTO {

    private Long requestId;

    private Long initiatorId;

    private Long cardId;

    private String status;

    private LocalDateTime requestedAt;

    private String message;
}
