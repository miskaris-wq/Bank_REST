package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import com.example.bankcards.entity.block.CardRequestStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InactiveCardException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.StatusAlreadySetException;
import com.example.bankcards.mappers.BankCardBalanceMapper;
import com.example.bankcards.mappers.BankCardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptionUtil;
import com.example.bankcards.util.CardNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankCardMapper bankCardMapper;

    @Mock
    private BankCardBalanceMapper bankCardBalanceMapper;

    @Mock
    private CardEncryptionUtil cardEncryptionUtil;

    @Mock
    private UserService userService;

    @Mock
    private CardRequestService cardRequestService;

    @Spy
    @InjectMocks
    private CardService cardService;

    private User user;
    private BankCard card;
    private BankCardDTO cardDto;
    private CardBalanceDto balanceDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();
        card = new BankCard();
        card.setId(2L);
        card.setStatus(Status.BLOCKED);
        cardDto = new BankCardDTO();
        balanceDto = new CardBalanceDto();
    }

    @Test
    void create_Success() {
        CreateCardRequest request = new CreateCardRequest();
        request.setOwnerId(1L);
        try (MockedStatic<CardNumberGenerator> mockedStatic = mockStatic(CardNumberGenerator.class)) {
            mockedStatic.when(CardNumberGenerator::generateCardNumber).thenReturn("1234 1234 1234 1234");
            when(cardEncryptionUtil.encrypt("1234 1234 1234 1234")).thenReturn("enc");
            when(cardEncryptionUtil.maskEncrypted("enc")).thenReturn("mask");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(bankCardMapper.toDto(any())).thenReturn(cardDto);

            BankCardDTO result = cardService.create(request);

            verify(cardRepository).save(any(BankCard.class));
            assertThat(result).isEqualTo(cardDto);
        }
    }

    @Test
    void create_UserNotFound() {
        CreateCardRequest request = new CreateCardRequest();
        request.setOwnerId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.create(request))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void activate_Success() {
        doReturn(cardDto).when(cardService).changeStatusCard(Status.ACTIVE, 2L);
        BankCardDTO result = cardService.activate(2L);
        assertThat(result).isEqualTo(cardDto);
    }

    @Test
    void blocked_Success() {
        doReturn(cardDto).when(cardService).changeStatusCard(Status.BLOCKED, 2L);
        when(cardRequestService.changeStatus(2L, CardRequestStatus.APPROVED)).thenReturn(null);
        BankCardDTO result = cardService.blocked(2L);
        assertThat(result).isEqualTo(cardDto);
    }

    @Test
    void delete_Success() {
        when(cardRepository.existsById(2L)).thenReturn(true);
        cardService.delete(2L);
        verify(cardRepository).deleteById(2L);
    }

    @Test
    void delete_NotFound() {
        when(cardRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> cardService.delete(2L))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void changeStatusCard_Success() {
        card.setStatus(Status.BLOCKED);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(bankCardMapper.toDto(card)).thenReturn(cardDto);
        BankCardDTO result = cardService.changeStatusCard(Status.ACTIVE, 2L);
        assertThat(result).isEqualTo(cardDto);
    }

    @Test
    void changeStatusCard_AlreadySet() {
        card.setStatus(Status.ACTIVE);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        assertThatThrownBy(() -> cardService.changeStatusCard(Status.ACTIVE, 2L))
                .isInstanceOf(StatusAlreadySetException.class);
    }

    @Test
    void getAllCurrentUser_Page_Success() {
        Page<BankCard> page = new PageImpl<>(List.of(card));
        when(cardRepository.findAllByOwnerId(PageRequest.of(0, 5), 1L)).thenReturn(page);
        when(bankCardMapper.toDto(card)).thenReturn(cardDto);
        Page<BankCardDTO> result = cardService.getAllCurrentUser(0,5,1L);
        assertThat(result.getContent()).containsExactly(cardDto);
    }

    @Test
    void getAllCurrentUser_Page_Empty() {
        when(cardRepository.findAllByOwnerId(PageRequest.of(0, 5), 1L)).thenReturn(Page.empty());
        assertThatThrownBy(() -> cardService.getAllCurrentUser(0,5,1L))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void getAllCurrentUser_List_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findAllByOwnerId(1L)).thenReturn(List.of(card));
        when(bankCardMapper.toDto(card)).thenReturn(cardDto);
        List<BankCardDTO> result = cardService.getAllCurrentUser();
        assertThat(result).containsExactly(cardDto);
    }

    @Test
    void getAllCurrentUser_List_Empty() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findAllByOwnerId(1L)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> cardService.getAllCurrentUser())
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void getAll_Success() {
        Page<BankCard> page = new PageImpl<>(List.of(card));
        when(cardRepository.findAll(PageRequest.of(0,5))).thenReturn(page);
        when(bankCardMapper.toDto(card)).thenReturn(cardDto);
        Page<BankCardDTO> result = cardService.getAll(0,5);
        assertThat(result.getContent()).containsExactly(cardDto);
    }

    @Test
    void getAll_Empty() {
        when(cardRepository.findAll(PageRequest.of(0,5))).thenReturn(Page.empty());
        assertThatThrownBy(() -> cardService.getAll(0,5))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void getById_Success() {
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(bankCardMapper.toDto(card)).thenReturn(cardDto);
        BankCardDTO result = cardService.getById(2L);
        assertThat(result).isEqualTo(cardDto);
    }

    @Test
    void getById_NotFound() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.getById(2L))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void isOwnerCard() {
        when(cardRepository.existsByIdAndOwnerId(2L,1L)).thenReturn(true);
        assertThat(cardService.isOwnerCard(1L,2L)).isTrue();
    }

    @Test
    void withdraw_Success() {
        card.setStatus(Status.ACTIVE);
        card.setBalance(BigDecimal.valueOf(100));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(bankCardBalanceMapper.toDto(card)).thenReturn(balanceDto);
        CardBalanceDto result = cardService.withdraw(2L, BigDecimal.valueOf(50));
        assertThat(result).isEqualTo(balanceDto);
    }

    @Test
    void withdraw_NotFound() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.withdraw(2L, BigDecimal.ONE))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void withdraw_NotActive() {
        card.setStatus(Status.BLOCKED);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        assertThatThrownBy(() -> cardService.withdraw(2L, BigDecimal.ONE))
                .isInstanceOf(InactiveCardException.class);
    }

    @Test
    void withdraw_InsufficientFunds() {
        card.setStatus(Status.ACTIVE);
        card.setBalance(BigDecimal.valueOf(10));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        assertThatThrownBy(() -> cardService.withdraw(2L, BigDecimal.valueOf(20)))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void deposit_Success() {
        card.setStatus(Status.ACTIVE);
        card.setBalance(BigDecimal.valueOf(100));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(bankCardBalanceMapper.toDto(card)).thenReturn(balanceDto);
        CardBalanceDto result = cardService.deposit(2L, BigDecimal.valueOf(50));
        assertThat(result).isEqualTo(balanceDto);
    }

    @Test
    void deposit_NotActive() {
        card.setStatus(Status.BLOCKED);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        assertThatThrownBy(() -> cardService.deposit(2L, BigDecimal.valueOf(50)))
                .isInstanceOf(InactiveCardException.class);
    }

    @Test
    void getBalance_Success() {
        when(cardRepository.findAllByOwnerIdAndId(1L,2L))
                .thenReturn(Optional.of(card));
        when(bankCardBalanceMapper.toDto(card)).thenReturn(balanceDto);
        CardBalanceDto result = cardService.getBalance(1L,2L);
        assertThat(result).isEqualTo(balanceDto);
    }

    @Test
    void getBalance_NotFound() {
        when(cardRepository.findAllByOwnerIdAndId(1L,2L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.getBalance(1L,2L))
                .isInstanceOf(CardNotFoundException.class);
    }

    @Test
    void isActivated() {
        card.setStatus(Status.ACTIVE);
        assertThat(cardService.isActivated(card)).isTrue();
    }
}
