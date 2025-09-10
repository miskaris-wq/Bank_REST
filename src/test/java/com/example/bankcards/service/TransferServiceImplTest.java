package com.example.bankcards.service;

import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import com.example.bankcards.entity.transfer.Transfer;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.InactiveCardException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.exception.TransferNotFoundException;
import com.example.bankcards.mappers.TransferMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.service.impl.TransferServiceImpl;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private CardServiceImpl cardServiceImpl;

    @Mock
    private TransferMapper transferMapper;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransferServiceImpl transferServiceImpl;

    @Captor
    private ArgumentCaptor<Transfer> transferCaptor;

    private BankCard fromCard;
    private BankCard toCard;
    private User user;
    private Transfer transfer;
    private TransferUserRequest request;
    private TransferUserDto dto;

    @BeforeEach
    void setUp() {
        fromCard = new BankCard();
        fromCard.setId(1L);
        fromCard.setStatus(Status.ACTIVE);
        toCard = new BankCard();
        toCard.setId(2L);
        toCard.setStatus(Status.ACTIVE);
        user = new User();
        user.setId(3L);
        transfer = new Transfer();
        transfer.setId(4L);
        request = new TransferUserRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(10));
        dto = new TransferUserDto();
    }

    @Test
    void transferFromToCardUser_Success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(transferRepository.save(any())).thenReturn(transfer);
        when(transferMapper.toDto(transfer)).thenReturn(dto);

        TransferUserDto result = transferServiceImpl.transferFromToCardUser(3L, request);

        verify(cardServiceImpl).withdraw(1L, BigDecimal.valueOf(10));
        verify(cardServiceImpl).deposit(2L, BigDecimal.valueOf(10));
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void transferFromToCardUser_FromCardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> transferServiceImpl.transferFromToCardUser(3L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void transferFromToCardUser_ToCardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> transferServiceImpl.transferFromToCardUser(3L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void transferFromToCardUser_InactiveCard() {
        toCard.setStatus(Status.BLOCKED);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        assertThatThrownBy(() -> transferServiceImpl.transferFromToCardUser(3L, request))
                .isInstanceOf(InactiveCardException.class);
    }

    @Test
    void transferFromToCardUser_UserNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> transferServiceImpl.transferFromToCardUser(3L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void transferFromToCardUser_TransferNotFound() {
        when(transferRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> transferServiceImpl.getById(5L))
                .isInstanceOf(TransferNotFoundException.class);
    }

    @Test
    void getAll_Success() {
        Page<Transfer> page = new PageImpl<>(List.of(transfer));
        when(transferRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
        when(transferMapper.toDto(transfer)).thenReturn(dto);
        Page<TransferUserDto> result = transferServiceImpl.getAll(0, 5);
        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void getById_Success() {
        when(transferRepository.findById(4L)).thenReturn(Optional.of(transfer));
        when(transferMapper.toDto(transfer)).thenReturn(dto);
        TransferUserDto result = transferServiceImpl.getById(4L);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getAllByUser_Success() {
        Page<Transfer> page = new PageImpl<>(List.of(transfer));
        when(transferRepository.findAllByInitiatorId(3L, PageRequest.of(0, 5))).thenReturn(page);
        when(transferMapper.toDto(transfer)).thenReturn(dto);
        Page<TransferUserDto> result = transferServiceImpl.getAllByUser(0, 5, 3L);
        assertThat(result.getContent()).containsExactly(dto);
    }
}
