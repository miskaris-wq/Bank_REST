package com.example.bankcards.config.init;

import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Компонент инициализации паролей пользователей.
 *
 * <p>После применения Liquibase миграций пароли в таблице {@code users}
 * могут быть сохранены в виде простого текста (например, "admin", "user").
 * Данный класс при старте приложения проверяет все записи в БД и
 * кодирует пароли через {@link PasswordEncoder}, если они ещё не зашифрованы.</p>
 *
 * <p>Повторное выполнение безопасно: если пароль уже в формате BCrypt
 * (начинается с "$2a$" или "$2b$"), он не будет перекодирован.</p>
 */
@RequiredArgsConstructor
@Component
public class PasswordEncoderInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Обрабатывает событие старта приложения и кодирует пароли пользователей,
     * если они хранятся в открытом виде.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void encodePasswords() {
        userRepository.findAll().forEach(user -> {
            String rawPassword = user.getPassword();

            if (!rawPassword.startsWith("$2a$") && !rawPassword.startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        });
    }
}
