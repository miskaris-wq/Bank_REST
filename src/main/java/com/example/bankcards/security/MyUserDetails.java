package com.example.bankcards.security;

import com.example.bankcards.entity.user.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Адаптер для интеграции сущности {@link User} с Spring Security.
 * <p>
 * Реализует интерфейсы {@link UserDetails} и {@link CredentialsContainer},
 * предоставляя данные пользователя для аутентификации и авторизации.
 * </p>
 */
public class MyUserDetails implements UserDetails, CredentialsContainer {

    private final User user;
    private String password;

    /**
     * Конструктор.
     *
     * @param user объект доменной сущности {@link User}
     */
    public MyUserDetails(User user) {
        this.user = user;
        this.password = user.getPassword();
    }

    /**
     * Возвращает список ролей пользователя в формате GrantedAuthority.
     *
     * @return коллекция прав доступа
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
                .toList();
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Возвращает имя пользователя (username).
     *
     * @return имя пользователя
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return id пользователя
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Стирает пароль из памяти для повышения безопасности.
     */
    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
