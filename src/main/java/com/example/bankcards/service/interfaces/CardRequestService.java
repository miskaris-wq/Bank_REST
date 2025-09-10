package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.CardRequestDTO;
import com.example.bankcards.entity.block.CardRequestStatus;
import org.springframework.data.domain.Page;

public interface CardRequestService {

    CardRequestDTO requestBlock(Long cardId);

    CardRequestDTO changeStatus(Long cardId, CardRequestStatus status);

    CardRequestDTO getId(Long id);

    Page<CardRequestDTO> getAllByUser(Long userId, int pageNumber, int pageSize);

    Page<CardRequestDTO> getAll(int pageNumber, int pageSize);

    CardRequestDTO requestRejected(Long id);

    boolean getIsOwnerCardRequest(Long cardRequestId, Long userId);

    CardRequestDTO getIdByUser(Long id, Long userId);
}

