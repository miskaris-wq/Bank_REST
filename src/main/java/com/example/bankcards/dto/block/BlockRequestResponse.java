package com.example.bankcards.dto.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
@Builder
public class BlockRequestResponse {
    Long id;
    Long cardId;
    Long requestedByUserId;
    String status;              // PENDING | APPROVED | REJECTED
    String comment;
    OffsetDateTime createdAt;
    OffsetDateTime resolvedAt;
}
