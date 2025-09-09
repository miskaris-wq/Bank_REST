package com.example.bankcards.service;

import com.example.bankcards.dto.block.BlockRequestCreate;
import com.example.bankcards.entity.*;
import com.example.bankcards.entity.block.BlockRequestStatus;
import com.example.bankcards.entity.block.CardBlockRequest;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BlockRequestService {

    private final CardRepository cards;
    private final CardBlockRequestRepository blockRequests;

    @Transactional
    public CardBlockRequest request(Long userId, BlockRequestCreate req) {
        Card card = cards.findByIdAndOwnerId(req.getCardId(), userId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        CardBlockRequest r = new CardBlockRequest();
        r.setCard(card);
        r.setRequestedBy(card.getOwner());
        r.setStatus(BlockRequestStatus.PENDING);
        r.setComment(req.getComment());
        return blockRequests.save(r);
    }

    public Page<CardBlockRequest> listForUser(Long userId, Pageable pageable) {
        return blockRequests.findAllByRequestedById(userId, pageable);
    }

    public Page<CardBlockRequest> listByStatus(BlockRequestStatus status, Pageable pageable) {
        return blockRequests.findAllByStatus(status, pageable);
    }

    @Transactional
    public CardBlockRequest approve(Long requestId) {
        CardBlockRequest r = blockRequests.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        r.setStatus(BlockRequestStatus.APPROVED);
        r.setResolvedAt(OffsetDateTime.now());

        Card c = r.getCard();
        c.setStatus(CardStatus.BLOCKED);
        return r;
    }

    @Transactional
    public CardBlockRequest reject(Long requestId, String reason) {
        CardBlockRequest r = blockRequests.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        r.setStatus(BlockRequestStatus.REJECTED);
        r.setComment(reason);
        r.setResolvedAt(OffsetDateTime.now());
        return r;
    }
}
