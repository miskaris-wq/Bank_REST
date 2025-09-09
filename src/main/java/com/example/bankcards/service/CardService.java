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
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptionUtil;
import com.example.bankcards.util.CardNumberGenerator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private final BankCardMapper bankCardMapper;
    private final BankCardBalanceMapper bankCardBalanceMapper;

    private final CardEncryptionUtil cardEncryptionUtil;
    private final UserService userService;
    private final CardRequestService cardRequestService;

    public CardService(CardRepository cardRepository, UserRepository userRepository, BankCardMapper bankCardMapper, BankCardBalanceMapper bankCardBalanceMapper, CardEncryptionUtil cardEncryptionUtil, UserService userService, CardRequestService cardRequestService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.bankCardMapper = bankCardMapper;
        this.bankCardBalanceMapper = bankCardBalanceMapper;
        this.cardEncryptionUtil = cardEncryptionUtil;
        this.userService = userService;
        this.cardRequestService = cardRequestService;
    }

    @Transactional
    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    public BankCardDTO create(CreateCardRequest request) {

        String cardNumber = CardNumberGenerator.generateCardNumber();
        String encrypt = cardEncryptionUtil.encrypt(cardNumber);
        String maskCardNumber = cardEncryptionUtil.maskEncrypted(encrypt);

        BankCard card = new BankCard();

        try {
            User user = userRepository.findById(request.getOwnerId()).orElseThrow(() -> new CardNotFoundException("Пользователь с таким ID не найден"));
            card.setOwner(user);
            card.setCardNumber(maskCardNumber);
        }catch (CardNotFoundException e){
            throw new CardNotFoundException(e.getMessage());
        }

        cardRepository.save(card);

        return bankCardMapper.toDto(card);
    }


    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    @Transactional
    public BankCardDTO blocked(Long id) {

        BankCardDTO bankCardDTO = changeStatusCard(Status.BLOCKED, id);

        cardRequestService.changeStatus(id, CardRequestStatus.APPROVED);

        return bankCardDTO;
    }

    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    @Transactional
    public BankCardDTO activate(Long id) {
            return changeStatusCard(Status.ACTIVE, id);
    }

    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException("Банковская карточка не найдена");
        }

        cardRepository.deleteById(id);
    }

    @Transactional
    public BankCardDTO changeStatusCard(Status status, Long id){

            BankCard card = cardRepository.findById(id)
                    .orElseThrow(() -> new CardNotFoundException("Карта с таким ID не найдена"));

            if(card.getStatus().equals(status)){
                throw new StatusAlreadySetException(String.format("Карта уже %s",status.getStatus().toLowerCase()));
            }

            card.setStatus(status);
            card = cardRepository.save(card);

        return bankCardMapper.toDto(card);
    }

    @Cacheable(value = "bankCardsUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<BankCardDTO> getAllCurrentUser(int pageNumber, int pageSize, Long userId) {
        Page<BankCard> bankCards = cardRepository.findAllByOwnerId(PageRequest.of(pageNumber, pageSize), userId);

        if(bankCards.isEmpty()){
            throw new CardNotFoundException("Банковских карточек пока нет");
        }

        return bankCards.map(bankCardMapper::toDto);
    }

    public List<BankCardDTO> getAllCurrentUser() {
        Long userId = userService.getCurrentUser().getId();
        List<BankCard> bankCards = cardRepository.findAllByOwnerId(userId);

        if(bankCards.isEmpty()){
            throw new CardNotFoundException("Банковских карточек пока нет");
        }

        return bankCards.stream().map(bankCardMapper::toDto).toList();
    }

    @Cacheable(value = "bankCards", key = "#pageNumber + '-' + #pageSize")
    public Page<BankCardDTO> getAll(Integer pageNumber, Integer pageSize) {
        Page<BankCard> cards = cardRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if(cards.isEmpty()){
            throw new CardNotFoundException("Пока банковских карточек нет");
        }

        return cards.map(bankCardMapper::toDto);
    }

    public BankCardDTO getById(Long id){

        BankCard bankCard = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(String.format("Банковская карточка с id %d не найдена", id)));

        return bankCardMapper.toDto(bankCard);
    }

    public boolean isOwnerCard(Long userId, Long cardId){
        return cardRepository.existsByIdAndOwnerId(cardId, userId);
    }

    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    @Transactional
    public CardBalanceDto withdraw(Long cardId, BigDecimal amount) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));

        if(!isActivated(card)){
            throw new InactiveCardException("Карта должна быть активирована");
        }

        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        card.setBalance(card.getBalance().subtract(amount));
        card = cardRepository.save(card);

        return bankCardBalanceMapper.toDto(card);
    }

    @CacheEvict(value = {
            "bankCardsUser",
            "bankCardsUserAll",
            "bankCards"
    }, allEntries = true)
    @Transactional
    public CardBalanceDto deposit(Long cardId, BigDecimal amount) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));

        if(!isActivated(card)){
            throw new InactiveCardException("Карта должна быть активирована");
        }

        card.setBalance(card.getBalance().add(amount));

        card = cardRepository.save(card);

        return bankCardBalanceMapper.toDto(card);
    }

    public CardBalanceDto getBalance(Long userId, Long cardId) {

        BankCard card = cardRepository.findAllByOwnerIdAndId(userId, cardId)
                .orElseThrow(() -> new CardNotFoundException("Пользователя с такой картой не существует"));

        return bankCardBalanceMapper.toDto(card);
    }

    public boolean isActivated(BankCard bankCard){
        return bankCard.getStatus().equals(Status.ACTIVE);
    }
}
