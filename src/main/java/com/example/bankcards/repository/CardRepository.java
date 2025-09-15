package com.example.bankcards.repository;

import com.example.bankcards.entity.bankcard.BankCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с банковскими картами ({@link BankCard}).
 * <p>
 * Предоставляет стандартные CRUD-операции через {@link org.springframework.data.jpa.repository.JpaRepository}
 * и дополнительные методы поиска по владельцу карты.
 * </p>
 *
 * @author ksenya
 */
@Repository
public interface CardRepository extends JpaRepository<BankCard, Long> {

    /**
     * Находит карту по её идентификатору.
     *
     * @param id идентификатор карты
     * @return {@link Optional} с картой, если найдена
     */
    @NonNull
    Optional<BankCard> findById(@NonNull Long id);

    /**
     * Находит все карты, принадлежащие пользователю, постранично.
     *
     * @param request параметры пагинации
     * @param id идентификатор владельца
     * @return страница карт пользователя
     */
    Page<BankCard> findAllByOwnerId(Pageable request, Long id);

    /**
     * Находит все карты, принадлежащие пользователю.
     *
     * @param id идентификатор владельца
     * @return список карт пользователя
     */
    List<BankCard> findAllByOwnerId(Long id);

    /**
     * Возвращает все карты постранично.
     *
     * @param request параметры пагинации
     * @return страница карт
     */
    @NonNull
    Page<BankCard> findAll(@NonNull Pageable request);

    /**
     * Проверяет, существует ли карта с указанным id у данного пользователя.
     *
     * @param id идентификатор карты
     * @param userId идентификатор владельца
     * @return true, если карта существует и принадлежит пользователю
     */
    boolean existsByIdAndOwnerId(Long id, Long userId);

    /**
     * Находит карту по идентификатору владельца и идентификатору карты.
     *
     * @param ownerId идентификатор владельца
     * @param id идентификатор карты
     * @return {@link Optional} с картой, если найдена
     */
    Optional<BankCard> findAllByOwnerIdAndId(Long ownerId, Long id);
}
