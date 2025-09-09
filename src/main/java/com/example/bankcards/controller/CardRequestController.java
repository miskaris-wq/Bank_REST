package com.example.bankcards.controller;

import com.example.bankcards.dto.block.BlockRequestCreate;
import com.example.bankcards.dto.block.BlockRequestResponse;
import com.example.bankcards.entity.block.BlockRequestStatus;
import com.example.bankcards.entity.block.CardBlockRequest;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CardRequestController {

    private final BlockRequestService blockRequestService;
    private final UserService userService;
    private final BlockRequestMapper blockRequestMapper;

    // ===== USER: создать + посмотреть свои заявки =====

    @PostMapping("/api/cards/{cardId}/block-requests")
    public BlockRequestResponse requestBlock(@PathVariable Long cardId,
                                             @Valid @RequestBody BlockRequestCreate req,
                                             Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        // синхронизируем path id с body
        BlockRequestCreate fixed = new BlockRequestCreate(cardId, req.getComment());
        return blockRequestMapper.toResponse(blockRequestService.request(userId, fixed));
    }

    @GetMapping("/api/card-requests")
    public Page<BlockRequestResponse> myRequests(@ParameterObject Pageable pageable, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        Page<CardBlockRequest> page = blockRequestService.listForUser(userId, pageable);
        return page.map(blockRequestMapper::toResponse);
    }

    // ===== ADMIN: списки и решения =====

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/card-requests")
    public Page<BlockRequestResponse> listByStatus(@RequestParam(defaultValue = "PENDING") String status,
                                                   @ParameterObject Pageable pageable) {
        Page<CardBlockRequest> page = blockRequestService
                .listByStatus(BlockRequestStatus.valueOf(status), pageable);
        return page.map(blockRequestMapper::toResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/card-requests/{id}/approve")
    public BlockRequestResponse approve(@PathVariable Long id) {
        return blockRequestMapper.toResponse(blockRequestService.approve(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/card-requests/{id}/reject")
    public BlockRequestResponse reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return blockRequestMapper.toResponse(blockRequestService.reject(id, reason));
    }
}
