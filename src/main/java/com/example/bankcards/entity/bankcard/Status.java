package com.example.bankcards.entity.bankcard;

import lombok.Getter;

/**
 * Перечисление статусов банковской карты.
 * <p>
 * Используется для обозначения текущего состояния карты
 * в системе.
 * </p>
 *
 * <ul>
 *     <li>{@link #ACTIVE} — карта активна и доступна для операций;</li>
 *     <li>{@link #BLOCKED} — карта заблокирована администратором или по заявке;</li>
 *     <li>{@link #EXPIRED} — срок действия карты истёк.</li>
 * </ul>
 *
 * @author ksenya
 */
@Getter
public enum Status {
    /**
     * Карта активна.
     */
    ACTIVE("Активна"),

    /**
     * Карта заблокирована.
     */
    BLOCKED("Заблокирована"),

    /**
     * Срок действия карты истёк.
     */
    EXPIRED("Истек срок");

    /**
     * Человекочитаемое описание статуса.
     */
    private final String newStatus;

    Status(String newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * Возвращает текстовое описание статуса для отображения.
     */
    @Override
    public String toString() {
        return newStatus;
    }
}
