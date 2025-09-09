package com.example.bankcards.repository;

import com.example.bankcards.entity.block.CardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    Page<CardRequest> findAllByInitiatorId(Long userId, Pageable of);

    Optional<CardRequest> findByCard_Id(Long cardId);

    boolean existsCardRequestByIdAndInitiator_Id(Long id, Long initiatorId);

    Optional<CardRequest> findByIdAndInitiator_Id(Long id, Long initiatorId);
}
