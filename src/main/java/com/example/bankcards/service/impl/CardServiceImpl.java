package com.example.bankcards.service.impl;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import com.example.bankcards.entity.block.CardRequestStatus;
import com.example.bankcards.exception.*;
import com.example.bankcards.mappers.BankCardBalanceMapper;
import com.example.bankcards.mappers.BankCardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.interfaces.CardService;
import com.example.bankcards.util.CardEncryptionUtil;
import com.example.bankcards.util.CardNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankCardMapper bankCardMapper;
    private final BankCardBalanceMapper bankCardBalanceMapper;
    private final CardEncryptionUtil cardEncryptionUtil;
    private final UserServiceImpl userServiceImpl;
    private final CardRequestServiceImpl cardRequestServiceImpl;

    @Override
    @Transactional
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public BankCardDTO create(CreateCardRequest request) {
        var user = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Владелец с ID " + request.getOwnerId() + " не найден"));

        String cardNumber = CardNumberGenerator.generateCardNumber();
        String encrypted = cardEncryptionUtil.encrypt(cardNumber);
        String masked = cardEncryptionUtil.maskEncrypted(encrypted);

        var card = BankCard.builder()
                .owner(user)
                .cardNumber(masked)
                .build();

        card = cardRepository.save(card);
        return bankCardMapper.toDto(card);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public BankCardDTO blocked(Long id) {
        BankCardDTO dto = changeStatusCard(Status.BLOCKED, id);
        cardRequestServiceImpl.changeStatus(id, CardRequestStatus.APPROVED);
        return dto;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public BankCardDTO activate(Long id) {
        return changeStatusCard(Status.ACTIVE, id);
    }

    @Override
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException("Карта с ID " + id + " не существует");
        }
        cardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BankCardDTO changeStatusCard(Status status, Long id) {
        BankCard card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));

        if (card.getStatus() == status) {
            throw new StatusAlreadySetException("Карта уже имеет статус: " + status.getStatus().toLowerCase());
        }

        card.setStatus(status);
        card = cardRepository.save(card);

        return bankCardMapper.toDto(card);
    }

    @Override
    @Cacheable(value = "bankCardsUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<BankCardDTO> getAllCurrentUser(int pageNumber, int pageSize, Long userId) {
        Page<BankCard> cards = cardRepository.findAllByOwnerId(PageRequest.of(pageNumber, pageSize), userId);
        if (cards.isEmpty()) {
            throw new CardNotFoundException("У пользователя с ID " + userId + " пока нет карт");
        }
        return cards.map(bankCardMapper::toDto);
    }

    @Override
    public List<BankCardDTO> getAllCurrentUser() {
        Long userId = userServiceImpl.getCurrentUser().getId();
        List<BankCard> cards = cardRepository.findAllByOwnerId(userId);
        if (cards.isEmpty()) {
            throw new CardNotFoundException("У пользователя с ID " + userId + " пока нет карт");
        }
        return cards.stream().map(bankCardMapper::toDto).toList();
    }

    @Override
    @Cacheable(value = "bankCards", key = "#pageNumber + '-' + #pageSize")
    public Page<BankCardDTO> getAll(Integer pageNumber, Integer pageSize) {
        Page<BankCard> cards = cardRepository.findAll(PageRequest.of(pageNumber, pageSize));
        if (cards.isEmpty()) {
            throw new CardNotFoundException("Банковские карты отсутствуют");
        }
        return cards.map(bankCardMapper::toDto);
    }

    @Override
    public BankCardDTO getById(Long id) {
        BankCard card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        return bankCardMapper.toDto(card);
    }

    @Override
    public boolean isOwnerCard(Long userId, Long cardId) {
        return cardRepository.existsByIdAndOwnerId(cardId, userId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public CardBalanceDTO withdraw(Long cardId, BigDecimal amount) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с ID " + cardId + " не найдена"));

        if (!isActivated(card)) {
            throw new InactiveCardException("Нельзя списывать деньги с неактивной карты");
        }
        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на карте");
        }

        card.setBalance(card.getBalance().subtract(amount));
        card = cardRepository.save(card);
        return bankCardBalanceMapper.toDto(card);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"bankCardsUser", "bankCardsUserAll", "bankCards"}, allEntries = true)
    public CardBalanceDTO deposit(Long cardId, BigDecimal amount) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с ID " + cardId + " не найдена"));

        if (!isActivated(card)) {
            throw new InactiveCardException("Нельзя пополнить неактивную карту");
        }

        card.setBalance(card.getBalance().add(amount));
        card = cardRepository.save(card);
        return bankCardBalanceMapper.toDto(card);
    }

    @Override
    public CardBalanceDTO getBalance(Long userId, Long cardId) {
        BankCard card = cardRepository.findAllByOwnerIdAndId(userId, cardId)
                .orElseThrow(() -> new CardNotFoundException("У пользователя с ID " + userId + " нет карты с ID " + cardId));
        return bankCardBalanceMapper.toDto(card);
    }

    @Override
    public boolean isActivated(BankCard bankCard) {
        return bankCard.getStatus() == Status.ACTIVE;
    }
}
