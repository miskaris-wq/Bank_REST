package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.transfer.CardTransfer;
import com.example.bankcards.entity.transfer.TransferStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.util.ExpiryUtils;
import com.example.bankcards.util.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final CardRepository cards;
    private final CardTransferRepository transfers;

    @Transactional
    public CardTransfer transfer(Long userId, TransferRequest req) {
        if (req.getFromId().equals(req.getToId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }
        MoneyUtils.requirePositive(req.getAmount());
        BigDecimal amount = MoneyUtils.normalize(req.getAmount());

        Card from = cards.findByIdAndOwnerId(req.getFromId(), userId)
                .orElseThrow(() -> new NoSuchElementException("Source card not found"));
        Card to = cards.findByIdAndOwnerId(req.getToId(), userId)
                .orElseThrow(() -> new NoSuchElementException("Target card not found"));

        requireActiveAndNotExpired(from);
        requireActiveAndNotExpired(to);

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        CardTransfer t = new CardTransfer();
        t.setFromCard(from);
        t.setToCard(to);
        t.setAmount(amount);
        t.setStatus(TransferStatus.PROCESS);
        t = transfers.save(t);

        from.setBalance(MoneyUtils.normalize(from.getBalance().subtract(amount)));
        to.setBalance(MoneyUtils.normalize(to.getBalance().add(amount)));

        t.setStatus(TransferStatus.COMPLETED);
        return t;
    }

    public Page<CardTransfer> historyForUser(Long userId, Pageable pageable) {
        return transfers.findAllForUser(userId, pageable);
    }

    private void requireActiveAndNotExpired(Card c) {
        if (c.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Card must be ACTIVE");
        }
        YearMonth ym = YearMonth.parse(c.getExpiry());
        if (ExpiryUtils.isExpired(ym)) {
            throw new IllegalStateException("Card is expired");
        }
    }
}
