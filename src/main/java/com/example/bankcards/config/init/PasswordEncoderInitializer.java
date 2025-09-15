package com.example.bankcards.config.init;

import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PasswordEncoderInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
