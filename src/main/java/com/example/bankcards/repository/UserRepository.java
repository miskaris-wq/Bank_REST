package com.example.bankcards.repository;

import com.example.bankcards.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * <p>
 * Содержит методы поиска пользователя по ID и имени.
 * </p>
 *
 * @author ksenya
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return {@link Optional}, содержащий {@link User}, если найден
     */
    Optional<User> findByUsername(String username);

}
