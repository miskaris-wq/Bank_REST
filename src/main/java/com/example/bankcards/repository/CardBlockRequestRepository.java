package com.example.bankcards.repository;

import com.example.bankcards.entity.block.CardBlockRequest;
import com.example.bankcards.entity.block.BlockRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {

    Page<CardBlockRequest> findAllByRequestedById(Long userId, Pageable pageable);

    Page<CardBlockRequest> findAllByStatus(BlockRequestStatus status, Pageable pageable);
}
