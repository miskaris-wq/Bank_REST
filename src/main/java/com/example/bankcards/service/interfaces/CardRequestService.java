package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.CardRequestDTO;
import com.example.bankcards.entity.block.CardRequestStatus;
import org.springframework.data.domain.Page;

/**
 * Сервис для работы с заявками на блокировку банковских карт.
 * <p>
 * Поддерживает создание заявок пользователями, их рассмотрение администраторами
 * и получение информации о заявках.
 * </p>
 */
public interface CardRequestService {

    /**
     * Создает новую заявку на блокировку карты.
     *
     * @param cardId идентификатор карты, для которой создается заявка
     * @return {@link CardRequestDTO} созданная заявка
     */
    CardRequestDTO requestBlock(Long cardId);

    /**
     * Изменяет статус существующей заявки.
     *
     * @param cardId идентификатор карты
     * @param status новый статус заявки
     * @return {@link CardRequestDTO} обновленная заявка
     */
    CardRequestDTO changeStatus(Long cardId, CardRequestStatus status);

    /**
     * Получает заявку по её идентификатору.
     *
     * @param id идентификатор заявки
     * @return {@link CardRequestDTO} найденная заявка
     */
    CardRequestDTO getId(Long id);

    /**
     * Возвращает все заявки, созданные конкретным пользователем.
     *
     * @param userId    идентификатор пользователя
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link Page} список заявок в постраничном формате
     */
    Page<CardRequestDTO> getAllByUser(Long userId, int pageNumber, int pageSize);

    /**
     * Возвращает список всех заявок в системе (для администратора).
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link Page} список заявок
     */
    Page<CardRequestDTO> getAll(int pageNumber, int pageSize);

    /**
     * Отклоняет заявку на блокировку карты.
     *
     * @param id идентификатор заявки
     * @return {@link CardRequestDTO} обновленная заявка со статусом {@code REJECTED}
     */
    CardRequestDTO requestRejected(Long id);

    /**
     * Проверяет, принадлежит ли заявка пользователю.
     *
     * @param cardRequestId идентификатор заявки
     * @param userId        идентификатор пользователя
     * @return {@code true}, если заявка принадлежит пользователю, иначе {@code false}
     */
    boolean getIsOwnerCardRequest(Long cardRequestId, Long userId);

    /**
     * Получает заявку по ID, если она принадлежит пользователю.
     *
     * @param id     идентификатор заявки
     * @param userId идентификатор пользователя
     * @return {@link CardRequestDTO} найденная заявка
     */
    CardRequestDTO getIdByUser(Long id, Long userId);
}
