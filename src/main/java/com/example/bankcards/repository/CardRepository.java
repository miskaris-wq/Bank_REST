package com.example.bankcards.repository;

import com.example.bankcards.entity.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    Page<Card> findAllByOwnerId(Long ownerId, Pageable pageable);

    Optional<Card> findByIdAndOwnerId(Long id, Long ownerId);
}
