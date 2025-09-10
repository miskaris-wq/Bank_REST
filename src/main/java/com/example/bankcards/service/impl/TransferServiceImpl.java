package com.example.bankcards.service.impl;

import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import com.example.bankcards.entity.transfer.Transfer;
import com.example.bankcards.entity.transfer.TransferStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.InactiveCardException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.exception.TransferNotFoundException;
import com.example.bankcards.mappers.TransferMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.interfaces.CardService;
import com.example.bankcards.service.interfaces.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardService cardServiceImpl;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(value = "allTransfers", allEntries = true)
    public TransferUserDto transferFromToCardUser(Long userId, TransferUserRequest request) {
        Long fromCardId = request.getFromCardId();
        Long toCardId   = request.getToCardId();
        BigDecimal amount = request.getAmount();

        BankCard fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Не найдена карта-отправитель с ID = " + fromCardId));
        BankCard toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Не найдена карта-получатель с ID = " + toCardId));

        if (!(fromCard.getStatus() == Status.ACTIVE && toCard.getStatus() == Status.ACTIVE)) {
            throw new InactiveCardException("Обе карты должны быть активированы для перевода");
        }

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Не удалось найти пользователя с ID = " + userId));

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .initiator(initiator)
                .amount(amount)
                .status(TransferStatus.PROCESS)
                .build();

        transfer = transferRepository.save(transfer);

        try {
            cardServiceImpl.withdraw(fromCardId, amount);
            cardServiceImpl.deposit(toCardId, amount);
            transfer.setStatus(TransferStatus.COMPLETED);
        } catch (RuntimeException ex) {
            transfer.setStatus(TransferStatus.CANCELLED);
            transferRepository.save(transfer);
            throw ex;
        }

        transfer = transferRepository.save(transfer);
        return transferMapper.toDto(transfer);
    }

    @Override
    @Cacheable(value = "allTransfers", key = "#pageNumber + '-' + #pageSize")
    public Page<TransferUserDto> getAll(int pageNumber, int pageSize) {
        Page<Transfer> transfers = transferRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if (transfers.isEmpty()) {
            throw new ResourceNotFoundException("Переводы пока отсутствуют");
        }

        return transfers.map(transferMapper::toDto);
    }

    @Override
    @Cacheable(value = "transfer", key = "#id")
    public TransferUserDto getById(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Не найден перевод с ID = " + id));

        return transferMapper.toDto(transfer);
    }

    @Override
    @Cacheable(value = "allTransfersUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<TransferUserDto> getAllByUser(int pageNumber, int pageSize, Long userId) {
        Page<Transfer> transfers = transferRepository.findAllByInitiatorId(userId, PageRequest.of(pageNumber, pageSize));

        if (transfers.isEmpty()) {
            throw new ResourceNotFoundException("У пользователя с ID " + userId + " пока нет переводов");
        }

        return transfers.map(transferMapper::toDto);
    }
}
