package com.example.bankcards.service;

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
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferService {

    private final CardService cardService;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "allTransfers", allEntries = true)
    public TransferUserDto transferFromToCardUser(Long userId, TransferUserRequest request) {
        Long fromCardId = request.getFromCardId();
        Long toCardId   = request.getToCardId();
        BigDecimal amount = request.getAmount();

        BankCard fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Карта-отправитель не найдена"));
        BankCard toCard   = cardRepository.findById(toCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Карта-получатель не найдена"));

        if(!(fromCard.getStatus().equals(Status.ACTIVE) && toCard.getStatus().equals(Status.ACTIVE))){
            throw new InactiveCardException("Обе карты должны быть актевированы");
        }

        User initiator    = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .initiator(initiator)
                .amount(amount)
                .build();

        transfer = transferRepository.save(transfer);

        try {
            cardService.withdraw(fromCardId, amount);
            cardService.deposit(toCardId, amount);
            transfer.setStatus(TransferStatus.COMPLETED);
        } catch (RuntimeException ex) {
            transfer.setStatus(TransferStatus.CANCELLED);
            transferRepository.save(transfer);
            throw ex;
        }

        transfer = transferRepository.save(transfer);
        return transferMapper.toDto(transfer);
    }

    @Cacheable(value = "allTransfers", key = "#pageNumber + '-' + #pageSize")
    public Page<TransferUserDto> getAll(int pageNumber, int pageSize) {

        Page<Transfer> transfers = transferRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return transfers.map(transferMapper::toDto);
    }

    @Cacheable(value = "transfer", key = "#id")
    public TransferUserDto getById(Long id) {

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException(String.format("Перевода с id %d не найден", id)));

        return transferMapper.toDto(transfer);
    }

    @Cacheable(value = "allTransfersUser", key = "#pageNumber + '-' + #pageSize + '-' + #userId")
    public Page<TransferUserDto> getAllByUser(int pageNumber, int pageSize, Long userId) {

        Page<Transfer> transfers = transferRepository.findAllByInitiatorId(userId,PageRequest.of(pageNumber, pageSize));

        return transfers.map(transferMapper::toDto);
    }
}
