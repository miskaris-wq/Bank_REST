package com.example.bankcards.repository;

import com.example.bankcards.entity.block.CardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с заявками на блокировку карт ({@link CardRequest}).
 * <p>
 * Содержит методы для поиска заявок по инициатору или карте.
 * </p>
 *
 * @author ksenya
 */
@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {

    /**
     * Находит все заявки, созданные указанным пользователем.
     *
     * @param userId идентификатор пользователя (инициатора)
     * @param pageable параметры пагинации
     * @return страница заявок пользователя
     */
    Page<CardRequest> findAllByInitiatorId(Long userId, Pageable pageable);

    /**
     * Находит заявку по идентификатору карты.
     *
     * @param cardId идентификатор карты
     * @return {@link Optional} с заявкой, если найдена
     */
    Optional<CardRequest> findByCard_Id(Long cardId);

    /**
     * Проверяет, существует ли заявка с указанным ID и инициатором.
     *
     * @param id идентификатор заявки
     * @param initiatorId идентификатор инициатора
     * @return true, если заявка существует
     */
    boolean existsCardRequestByIdAndInitiator_Id(Long id, Long initiatorId);

    /**
     * Находит заявку по её идентификатору и идентификатору инициатора.
     *
     * @param id идентификатор заявки
     * @param initiatorId идентификатор инициатора
     * @return {@link Optional} с заявкой, если найдена
     */
    Optional<CardRequest> findByIdAndInitiator_Id(Long id, Long initiatorId);
}
