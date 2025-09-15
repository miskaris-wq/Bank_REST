package com.example.bankcards.repository;

import com.example.bankcards.entity.transfer.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с переводами ({@link Transfer}).
 * <p>
 * Содержит методы для поиска переводов по инициатору.
 * </p>
 *
 * @author ksenya
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    /**
     * Находит все переводы, инициированные конкретным пользователем.
     *
     * @param initiatorId идентификатор инициатора перевода
     * @param pageable параметры пагинации
     * @return страница переводов указанного пользователя
     */
    Page<Transfer> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}
