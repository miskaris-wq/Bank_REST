package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.block.CardRequest;
import com.example.bankcards.entity.block.CardRequestStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mappers.CardRequestMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CardRequestService {

    private final CardRepository cardRepository;
    private final CardRequestRepository cardRequestRepository;
    private final CardRequestMapper cardRequestMapper;
    private final UserService userService;

    public CardRequestService(CardRepository cardRepository, CardRequestRepository cardRequestRepository, CardRequestMapper cardRequestMapper, UserService userService) {
        this.cardRepository = cardRepository;
        this.cardRequestRepository = cardRequestRepository;
        this.cardRequestMapper = cardRequestMapper;
        this.userService = userService;
    }


    @Transactional
    @CacheEvict(value = {
            "cardRequestByUser",
            "cardRequests",
            "cardRequestsByUser"
    }, allEntries = true)
    public CardRequestDTO requestBlock(Long id) {
        BankCard card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Карта с id=%d не найдена", id))
                );

        User user = userService.getCurrentUser();

        CardRequest blockRequest = CardRequest.builder()
                .card(card)
                .status(CardRequestStatus.PENDING)
                .initiator(user)
                .requestedAt(LocalDateTime.now())
                .message("Запрос на блокировку создан пользователем")
                .build();

        CardRequest saved = cardRequestRepository.save(blockRequest);

        return CardRequestDTO.builder()
                .requestId(saved.getId())
                .cardId(card.getId())
                .status(saved.getStatus().name())
                .requestedAt(saved.getRequestedAt())
                .message(saved.getMessage())
                .build();
    }

    @CacheEvict(value = {
            "cardRequestByUser",
            "cardRequests",
            "cardRequestsByUser"
    }, allEntries = true)
    public CardRequestDTO changeStatus(Long cardId, CardRequestStatus status){
        CardRequest cardRequest = cardRequestRepository.findByCard_Id(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Запроса с такой карточкой не существует"));

        cardRequest.setStatus(status);

        cardRequest = cardRequestRepository.save(cardRequest);

        return cardRequestMapper.toDto(cardRequest);
    }

    @Cacheable(value = "cardRequest", key = "#id")
    public CardRequestDTO getId(Long id) {

        CardRequest cardRequest = cardRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запроса по данному ID не найдено"));

        return cardRequestMapper.toDto(cardRequest);
    }

    @Cacheable(value = "cardRequestsByUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<CardRequestDTO> getAllByUser(Long userId, int pageNumber, int pageSize) {

        Page<CardRequest>cardRequests = cardRequestRepository.findAllByInitiatorId(userId, PageRequest.of(pageNumber, pageSize));

        if(cardRequests.isEmpty()){
            throw new ResourceNotFoundException("Пользователь пока не отправлял запросов");
        }

        return cardRequests.map(cardRequestMapper::toDto);
    }

    @Cacheable(value = "cardRequests", key = "#pageNumber + '-' + #pageSize")
    public Page<CardRequestDTO> getAll(int pageNumber, int pageSize) {

        Page<CardRequest> cardRequests = cardRequestRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if(cardRequests.isEmpty()){
            throw new ResourceNotFoundException("Пока запросов нет");
        }

        return cardRequests.map(cardRequestMapper::toDto);
    }

    public CardRequestDTO requestRejected(Long id) {

        cardRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запроса с таким ID не существует"));

        return changeStatus(id, CardRequestStatus.REJECTED);
    }

    public boolean getIsOwnerCardRequest(Long cardRequestId, Long userId) {
        if(cardRequestId == null || userId == null) {
            return false;
        }

        return cardRequestRepository.existsCardRequestByIdAndInitiator_Id(cardRequestId, userId);
    }

    @Cacheable(value = "cardRequestByUser", key = "#id + '-' + #userId")
    public CardRequestDTO getIdByUser(Long id, Long userId) {
        CardRequest cardRequest = cardRequestRepository.findByIdAndInitiator_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Запроса по данному ID не найдено"));

        return cardRequestMapper.toDto(cardRequest);
    }
}
