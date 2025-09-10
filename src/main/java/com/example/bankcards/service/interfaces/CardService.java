package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {

    BankCardDTO create(CreateCardRequest request);

    BankCardDTO blocked(Long id);

    BankCardDTO activate(Long id);

    void delete(Long id);

    BankCardDTO changeStatusCard(Status status, Long id);

    Page<BankCardDTO> getAllCurrentUser(int pageNumber, int pageSize, Long userId);

    List<BankCardDTO> getAllCurrentUser();

    Page<BankCardDTO> getAll(Integer pageNumber, Integer pageSize);

    BankCardDTO getById(Long id);

    boolean isOwnerCard(Long userId, Long cardId);

    CardBalanceDTO withdraw(Long cardId, BigDecimal amount);

    CardBalanceDTO deposit(Long cardId, BigDecimal amount);

    CardBalanceDTO getBalance(Long userId, Long cardId);

    boolean isActivated(BankCard bankCard);
}
