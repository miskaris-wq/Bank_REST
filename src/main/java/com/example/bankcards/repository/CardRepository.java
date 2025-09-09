package com.example.bankcards.repository;

import com.example.bankcards.entity.bankcard.BankCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<BankCard, Long> {

    @NonNull
    Optional<BankCard> findById(@NonNull Long id);

    Page<BankCard> findAllByOwnerId(Pageable request, Long id);

    List<BankCard> findAllByOwnerId(Long id);

    @NonNull
    Page<BankCard> findAll(@NonNull Pageable request);

    boolean existsByIdAndOwnerId(Long id, Long userId);

    Optional<BankCard> findAllByOwnerIdAndId(Long ownerId, Long id);
}
