package com.example.bankcards.entity.user;

import lombok.Getter;

/**
 * Роли пользователей в системе.
 * <p>
 * Роль определяет права доступа к различным ресурсам и операциям:
 * </p>
 *
 * <ul>
 *     <li>{@link #USER} — обычный пользователь, имеет доступ только к своим данным (карты, переводы, заявки);</li>
 *     <li>{@link #ADMIN} — администратор, обладает полным доступом к системе.</li>
 * </ul>
 *
 * @author ksenya
 */
@Getter
public enum Role {

    /** Роль обычного пользователя. */
    USER("USER"),

    /** Роль администратора. */
    ADMIN("ADMIN");

    /** Строковое представление роли (используется в Spring Security). */
    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
