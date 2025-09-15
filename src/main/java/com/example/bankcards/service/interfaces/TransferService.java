package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.requests.TransferUserRequest;
import com.example.bankcards.dto.payload.TransferUserDto;
import org.springframework.data.domain.Page;

/**
 * Сервис для работы с переводами между банковскими картами.
 * <p>
 * Поддерживает выполнение переводов, получение истории переводов
 * как для администратора, так и для конкретного пользователя.
 * </p>
 */
public interface TransferService {

    /**
     * Выполняет перевод между картами пользователя.
     *
     * @param userId  идентификатор пользователя, инициирующего перевод
     * @param request данные перевода (откуда, куда, сумма)
     * @return {@link TransferUserDto} информация о выполненном переводе
     */
    TransferUserDto transferFromToCardUser(Long userId, TransferUserRequest request);

    /**
     * Возвращает список всех переводов (для администратора).
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link Page} список переводов
     */
    Page<TransferUserDto> getAll(int pageNumber, int pageSize);

    /**
     * Возвращает перевод по его идентификатору.
     *
     * @param id идентификатор перевода
     * @return {@link TransferUserDto} детали перевода
     */
    TransferUserDto getById(Long id);

    /**
     * Возвращает список переводов, выполненных конкретным пользователем.
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @param userId     идентификатор пользователя
     * @return {@link Page} список переводов пользователя
     */
    Page<TransferUserDto> getAllByUser(int pageNumber, int pageSize, Long userId);
}
