package com.example.bankcards.controller;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserService userService;
    private final CardMapper cardMapper;

    // ===== USER endpoints =====
    @GetMapping("/api/cards")
    public Page<CardResponse> listMyCards(@ParameterObject Pageable pageable,
                                          @ParameterObject CardFilterRequest filter,
                                          Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        Page<Card> page = cardService.listMine(userId, filter, pageable);
        return page.map(cardMapper::toResponse);
    }

    @GetMapping("/api/cards/{id}")
    public CardResponse getMyCard(@PathVariable Long id, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        Card c = cardService.getMine(userId, id);
        return cardMapper.toResponse(c);
    }

    // ===== ADMIN endpoints =====
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/cards")
    public ResponseEntity<CardResponse> create(@Valid @RequestBody CardCreateRequest req) {
        if (req.getOwnerId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Card saved = cardService.createForUser(req.getOwnerId(), req);
        return ResponseEntity.ok(cardMapper.toResponse(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/admin/cards/{id}/block")
    public CardResponse block(@PathVariable Long id) {
        return cardMapper.toResponse(cardService.changeStatus(id, CardStatus.BLOCKED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/admin/cards/{id}/activate")
    public CardResponse activate(@PathVariable Long id) {
        return cardMapper.toResponse(cardService.changeStatus(id, CardStatus.ACTIVE));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/admin/cards/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/cards")
    public Page<CardResponse> listAll(@ParameterObject Pageable pageable,
                                      @ParameterObject CardFilterRequest filter) {
        Page<Card> page = cardService.listAllForAdmin(filter, pageable);
        return page.map(cardMapper::toResponse);
    }
}
