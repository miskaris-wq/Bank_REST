package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardFilterRequest;
import com.example.bankcards.entity.*;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.ExpiryUtils;
import com.example.bankcards.util.MaskingUtils;
import com.example.bankcards.util.MoneyUtils;
import com.example.bankcards.util.PanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cards;
    private final UserRepository users;

    // ADMIN
    public Card createForUser(Long ownerId, CardCreateRequest req) {
        PanUtils.requireValid(req.getCardNumber());
        YearMonth ym = ExpiryUtils.parse(req.getExpiry());
        ExpiryUtils.requireNotExpired(ym);

        User owner = users.findById(ownerId).orElseThrow(() -> new NoSuchElementException("Owner not found"));

        Card card = new Card();
        card.setOwner(owner);
        card.setCardNumber(MaskingUtils.normalizePan(req.getCardNumber())); // шифруется конвертером
        card.setHolder(req.getHolder());
        card.setExpiry(req.getExpiry());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(MoneyUtils.normalize(new BigDecimal("0.00")));
        return cards.save(card);
    }

    // USER
    public Page<Card> listMine(Long userId, CardFilterRequest filter, Pageable pageable) {
        Specification<Card> spec = byOwner(userId).and(applyFilter(filter));
        return cards.findAll(spec, pageable);
    }

    // ADMIN
    public Page<Card> listAllForAdmin(CardFilterRequest filter, Pageable pageable) {
        return cards.findAll(applyFilter(filter), pageable);
    }

    // USER
    public Card getMine(Long userId, Long cardId) {
        return cards.findByIdAndOwnerId(cardId, userId).orElseThrow(() -> new NoSuchElementException("Card not found"));
    }

    // ADMIN
    public Card changeStatus(Long cardId, CardStatus status) {
        Card c = cards.findById(cardId).orElseThrow(() -> new NoSuchElementException("Card not found"));
        c.setStatus(status);
        return cards.save(c);
    }

    // ADMIN
    public void delete(Long cardId) {
        if (!cards.existsById(cardId)) throw new NoSuchElementException("Card not found");
        cards.deleteById(cardId);
    }

    private Specification<Card> byOwner(Long ownerId) {
        return (root, q, cb) -> cb.equal(root.get("owner").get("id"), ownerId);
    }
    private Specification<Card> applyFilter(CardFilterRequest f) {
        return (root, q, cb) -> {
            var p = cb.conjunction();
            if (f != null) {
                if (f.getStatus() != null && !f.getStatus().isBlank()) {
                    p = cb.and(p, cb.equal(root.get("status"), CardStatus.valueOf(f.getStatus())));
                }
                if (f.getLast4() != null && !f.getLast4().isBlank()) {
                    p = cb.and(p, cb.like(root.get("last4"), "%" + f.getLast4()));
                }
            }
            return p;
        };
    }
}
