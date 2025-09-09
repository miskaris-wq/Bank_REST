package com.example.bankcards.mapper;

import com.example.bankcards.dto.block.BlockRequestResponse;
import com.example.bankcards.entity.block.CardBlockRequest;
import org.springframework.stereotype.Component;

@Component
public class BlockRequestMapper {

    public BlockRequestResponse toResponse(CardBlockRequest r) {
        if (r == null) return null;
        return BlockRequestResponse.builder()
                .id(r.getId())
                .cardId(r.getCard().getId())
                .requestedByUserId(r.getRequestedBy().getId())
                .status(r.getStatus().name())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .resolvedAt(r.getResolvedAt())
                .build();
    }
}
