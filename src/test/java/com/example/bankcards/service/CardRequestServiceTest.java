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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardRequestServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardRequestRepository cardRequestRepository;

    @Mock
    private CardRequestMapper cardRequestMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private CardRequestService cardRequestService;

    @Captor
    private ArgumentCaptor<CardRequest> requestCaptor;

    private BankCard card;
    private User user;
    private CardRequest entity;
    private CardRequestDTO dto;

    @BeforeEach
    void setUp() {
        card = BankCard.builder().id(1L).build();
        user = User.builder().id(2L).build();
        entity = CardRequest.builder()
                .id(3L)
                .card(card)
                .status(CardRequestStatus.PENDING)
                .initiator(user)
                .requestedAt(LocalDateTime.now())
                .message("msg")
                .build();
        dto = CardRequestDTO.builder()
                .requestId(3L)
                .cardId(1L)
                .status("PENDING")
                .requestedAt(entity.getRequestedAt())
                .message("msg")
                .build();
    }

    @Test
    void requestBlock_Success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRequestRepository.save(any())).thenReturn(entity);

        CardRequestDTO result = cardRequestService.requestBlock(1L);

        verify(cardRequestRepository).save(requestCaptor.capture());
        CardRequest saved = requestCaptor.getValue();
        assertThat(saved.getCard()).isEqualTo(card);
        assertThat(saved.getInitiator()).isEqualTo(user);
        assertThat(result.getRequestId()).isEqualTo(3L);
        assertThat(result.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void requestBlock_NotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardRequestService.requestBlock(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void changeStatus_Success() {
        when(cardRequestRepository.findByCard_Id(1L)).thenReturn(Optional.of(entity));
        when(cardRequestRepository.save(any())).thenReturn(entity);
        when(cardRequestMapper.toDto(any())).thenReturn(dto);

        CardRequestDTO result = cardRequestService.changeStatus(1L, CardRequestStatus.APPROVED);

        assertThat(result.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void changeStatus_NotFound() {
        when(cardRequestRepository.findByCard_Id(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardRequestService.changeStatus(1L, CardRequestStatus.APPROVED))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getId_Success() {
        when(cardRequestRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(cardRequestMapper.toDto(entity)).thenReturn(dto);

        CardRequestDTO result = cardRequestService.getId(3L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getId_NotFound() {
        when(cardRequestRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardRequestService.getId(3L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllByUser_Success() {
        Page<CardRequest> page = new PageImpl<>(List.of(entity));
        when(cardRequestRepository.findAllByInitiatorId(2L, PageRequest.of(0, 5))).thenReturn(page);
        when(cardRequestMapper.toDto(entity)).thenReturn(dto);

        Page<CardRequestDTO> result = cardRequestService.getAllByUser(2L, 0, 5);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void getAllByUser_Empty() {
        when(cardRequestRepository.findAllByInitiatorId(2L, PageRequest.of(0, 5)))
                .thenReturn(Page.empty());
        assertThatThrownBy(() -> cardRequestService.getAllByUser(2L, 0, 5))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_Success() {
        Page<CardRequest> page = new PageImpl<>(List.of(entity));
        when(cardRequestRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
        when(cardRequestMapper.toDto(entity)).thenReturn(dto);

        Page<CardRequestDTO> result = cardRequestService.getAll(0, 5);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void getAll_Empty() {
        when(cardRequestRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());
        assertThatThrownBy(() -> cardRequestService.getAll(0, 5))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void requestRejected_Success() {
        when(cardRequestRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(cardRequestRepository.findByCard_Id(3L)).thenReturn(Optional.of(entity));
        when(cardRequestRepository.save(any())).thenReturn(entity);
        when(cardRequestMapper.toDto(any())).thenReturn(dto);

        CardRequestDTO result = cardRequestService.requestRejected(3L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void requestRejected_NotFound() {
        when(cardRequestRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardRequestService.requestRejected(3L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getIsOwnerCardRequest() {
        when(cardRequestRepository.existsCardRequestByIdAndInitiator_Id(3L, 2L)).thenReturn(true);
        boolean result = cardRequestService.getIsOwnerCardRequest(3L, 2L);
        assertThat(result).isTrue();
        assertThat(cardRequestService.getIsOwnerCardRequest(null, null)).isFalse();
    }

    @Test
    void getIdByUser_Success() {
        when(cardRequestRepository.findByIdAndInitiator_Id(3L, 2L))
                .thenReturn(Optional.of(entity));
        when(cardRequestMapper.toDto(entity)).thenReturn(dto);

        CardRequestDTO result = cardRequestService.getIdByUser(3L, 2L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getIdByUser_NotFound() {
        when(cardRequestRepository.findByIdAndInitiator_Id(3L, 2L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardRequestService.getIdByUser(3L, 2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
