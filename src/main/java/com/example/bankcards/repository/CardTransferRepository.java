package com.example.bankcards.repository;

import com.example.bankcards.entity.transfer.CardTransfer;
import com.example.bankcards.entity.transfer.TransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

public interface CardTransferRepository extends JpaRepository<CardTransfer, Long> {

    // История переводов пользователя (как отправителя, так и получателя)
    @Query("""
           select t from CardTransfer t
           where t.fromCard.owner.id = :userId or t.toCard.owner.id = :userId
           """)
    Page<CardTransfer> findAllForUser(Long userId, Pageable pageable);

    Page<CardTransfer> findAllByStatus(TransferStatus status, Pageable pageable);
}
