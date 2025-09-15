package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.dto.requests.CreateCardRequest;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.bankcard.Status;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для управления банковскими картами.
 * <p>
 * Поддерживает создание, изменение статуса, удаление карт,
 * а также операции пополнения и снятия средств.
 * </p>
 */
public interface CardService {

    /**
     * Создает новую банковскую карту для пользователя.
     *
     * @param request DTO с данными для создания карты
     * @return {@link BankCardDTO} созданная карта
     */
    BankCardDTO create(CreateCardRequest request);

    /**
     * Блокирует карту по её идентификатору.
     *
     * @param id идентификатор карты
     * @return {@link BankCardDTO} обновленная карта
     */
    BankCardDTO blocked(Long id);

    /**
     * Активирует карту по её идентификатору.
     *
     * @param id идентификатор карты
     * @return {@link BankCardDTO} обновленная карта
     */
    BankCardDTO activate(Long id);

    /**
     * Удаляет карту по её идентификатору.
     *
     * @param id идентификатор карты
     */
    void delete(Long id);

    /**
     * Изменяет статус карты.
     *
     * @param status новый статус карты
     * @param id     идентификатор карты
     * @return {@link BankCardDTO} обновленная карта
     */
    BankCardDTO changeStatusCard(Status status, Long id);

    /**
     * Возвращает список карт, принадлежащих конкретному пользователю (с постраничностью).
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @param userId     идентификатор пользователя
     * @return {@link Page} список карт
     */
    Page<BankCardDTO> getAllCurrentUser(int pageNumber, int pageSize, Long userId);

    /**
     * Возвращает список всех карт конкретного пользователя.
     *
     * @return список карт пользователя
     */
    List<BankCardDTO> getAllCurrentUser();

    /**
     * Возвращает список всех карт банка (для администратора).
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link Page} список карт
     */
    Page<BankCardDTO> getAll(Integer pageNumber, Integer pageSize);

    /**
     * Получает карту по её идентификатору.
     *
     * @param id идентификатор карты
     * @return {@link BankCardDTO} карта
     */
    BankCardDTO getById(Long id);

    /**
     * Проверяет, принадлежит ли карта пользователю.
     *
     * @param userId идентификатор пользователя
     * @param cardId идентификатор карты
     * @return {@code true}, если карта принадлежит пользователю, иначе {@code false}
     */
    boolean isOwnerCard(Long userId, Long cardId);

    /**
     * Списывает средства с карты.
     *
     * @param cardId идентификатор карты
     * @param amount сумма списания
     * @return {@link CardBalanceDTO} обновленный баланс
     */
    CardBalanceDTO withdraw(Long cardId, BigDecimal amount);

    /**
     * Пополняет карту на указанную сумму.
     *
     * @param cardId идентификатор карты
     * @param amount сумма пополнения
     * @return {@link CardBalanceDTO} обновленный баланс
     */
    CardBalanceDTO deposit(Long cardId, BigDecimal amount);

    /**
     * Возвращает баланс карты для конкретного пользователя.
     *
     * @param userId идентификатор пользователя
     * @param cardId идентификатор карты
     * @return {@link CardBalanceDTO} информация о балансе
     */
    CardBalanceDTO getBalance(Long userId, Long cardId);

    /**
     * Проверяет, активирована ли карта.
     *
     * @param bankCard сущность карты
     * @return {@code true}, если карта активна, иначе {@code false}
     */
    boolean isActivated(BankCard bankCard);
}
