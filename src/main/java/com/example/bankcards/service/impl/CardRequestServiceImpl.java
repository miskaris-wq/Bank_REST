package com.example.bankcards.service.impl;

import com.example.bankcards.dto.payload.CardRequestDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.block.CardRequest;
import com.example.bankcards.entity.block.CardRequestStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mappers.CardRequestMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.service.interfaces.CardRequestService;
import com.example.bankcards.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRepository cardRepository;
    private final CardRequestRepository cardRequestRepository;
    private final CardRequestMapper cardRequestMapper;
    private final UserService userServiceImpl;

    @Override
    @Transactional
    @CacheEvict(value = {"cardRequestByUser", "cardRequests", "cardRequestsByUser"}, allEntries = true)
    public CardRequestDTO requestBlock(Long cardId) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Не удалось найти карту с ID = " + cardId
                ));

        User user = userServiceImpl.getCurrentUser();

        CardRequest blockRequest = CardRequest.builder()
                .card(card)
                .status(CardRequestStatus.PENDING)
                .initiator(user)
                .requestedAt(LocalDateTime.now())
                .message("Пользователь создал заявку на блокировку карты")
                .build();

        CardRequest saved = cardRequestRepository.save(blockRequest);

        return cardRequestMapper.toDto(saved);
    }

    @Override
    @CacheEvict(value = {"cardRequestByUser", "cardRequests", "cardRequestsByUser"}, allEntries = true)
    public CardRequestDTO changeStatus(Long cardId, CardRequestStatus status) {
        CardRequest cardRequest = cardRequestRepository.findByCard_Id(cardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Запрос для карты с ID " + cardId + " не найден"
                ));

        cardRequest.setStatus(status);
        CardRequest updated = cardRequestRepository.save(cardRequest);

        return cardRequestMapper.toDto(updated);
    }

    @Override
    @Cacheable(value = "cardRequest", key = "#id")
    public CardRequestDTO getId(Long id) {
        CardRequest cardRequest = cardRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Не найдено запроса по ID " + id
                ));
        return cardRequestMapper.toDto(cardRequest);
    }

    @Override
    @Cacheable(value = "cardRequestsByUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<CardRequestDTO> getAllByUser(Long userId, int pageNumber, int pageSize) {
        Page<CardRequest> requests = cardRequestRepository.findAllByInitiatorId(userId, PageRequest.of(pageNumber, pageSize));

        if (requests.isEmpty()) {
            throw new ResourceNotFoundException("У пользователя с ID " + userId + " пока нет заявок");
        }

        return requests.map(cardRequestMapper::toDto);
    }

    @Override
    @Cacheable(value = "cardRequests", key = "#pageNumber + '-' + #pageSize")
    public Page<CardRequestDTO> getAll(int pageNumber, int pageSize) {
        Page<CardRequest> requests = cardRequestRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if (requests.isEmpty()) {
            throw new ResourceNotFoundException("Заявки на блокировку отсутствуют");
        }

        return requests.map(cardRequestMapper::toDto);
    }

    @Override
    public CardRequestDTO requestRejected(Long id) {
        cardRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Не найден запрос с ID " + id
                ));

        return changeStatus(id, CardRequestStatus.REJECTED);
    }

    @Override
    public boolean getIsOwnerCardRequest(Long cardRequestId, Long userId) {
        if (cardRequestId == null || userId == null) {
            return false;
        }
        return cardRequestRepository.existsCardRequestByIdAndInitiator_Id(cardRequestId, userId);
    }

    @Override
    @Cacheable(value = "cardRequestByUser", key = "#id + '-' + #userId")
    public CardRequestDTO getIdByUser(Long id, Long userId) {
        CardRequest cardRequest = cardRequestRepository.findByIdAndInitiator_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Заявка с ID " + id + " у пользователя " + userId + " не найдена"
                ));

        return cardRequestMapper.toDto(cardRequest);
    }
}
