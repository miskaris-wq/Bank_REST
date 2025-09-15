package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.payload.TotalCardBalanceDTO;
import com.example.bankcards.dto.payload.UserDTO;
import com.example.bankcards.entity.user.User;
import org.springframework.data.domain.Page;

/**
 * Сервис для управления пользователями.
 * <p>
 * Поддерживает получение информации о пользователях,
 * удаление, обновление данных и расчёт общего баланса карт.
 * </p>
 */
public interface UserService {

    /**
     * Находит пользователя по имени.
     *
     * @param username имя пользователя
     * @return {@link User} сущность пользователя
     */
    User getUserByUsername(String username);

    /**
     * Возвращает текущего авторизованного пользователя.
     *
     * @return {@link User} текущий пользователь
     */
    User getCurrentUser();

    /**
     * Возвращает список пользователей постранично.
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link Page} список пользователей в виде {@link UserDTO}
     */
    Page<UserDTO> getUserByUsername(int pageNumber, int pageSize);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return {@link UserDTO} данные пользователя
     */
    UserDTO getUserById(Long id);

    /**
     * Возвращает суммарный баланс всех карт пользователя.
     *
     * @param userId идентификатор пользователя
     * @return {@link TotalCardBalanceDTO} баланс пользователя
     */
    TotalCardBalanceDTO getTotalBalance(Long userId);

    /**
     * Обновляет данные пользователя.
     *
     * @param id      идентификатор пользователя
     * @param userDTO новые данные
     * @return {@link UserDTO} обновлённые данные пользователя
     */
    UserDTO update(Long id, UserDTO userDTO);
}
