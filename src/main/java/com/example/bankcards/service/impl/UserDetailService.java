package com.example.bankcards.service.impl;

import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link UserDetailsService}, используемая Spring Security
 * для загрузки данных пользователя при аутентификации.
 */
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает пользователя по имени.
     *
     * @param username имя пользователя
     * @return {@link UserDetails} данные о пользователе
     * @throws UsernameNotFoundException если пользователь с таким именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return new MyUserDetails(user);
    }
}
