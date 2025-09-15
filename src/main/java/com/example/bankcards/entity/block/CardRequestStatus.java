package com.example.bankcards.entity.block;

import lombok.Getter;

/**
 * Перечисление статусов заявки на блокировку банковской карты.
 * <p>
 * Используется для отображения текущего состояния заявки,
 * созданной пользователем и обрабатываемой администратором.
 * </p>
 *
 * <ul>
 *     <li>{@link #PENDING} — заявка создана и ожидает рассмотрения;</li>
 *     <li>{@link #APPROVED} — заявка одобрена администратором, карта будет заблокирована;</li>
 *     <li>{@link #REJECTED} — заявка отклонена администратором.</li>
 * </ul>
 *
 * @author ksenya
 */
@Getter
public enum CardRequestStatus {
    /**
     * Заявка ожидает рассмотрения.
     */
    PENDING("Заявка ожидает рассмотрения"),

    /**
     * Заявка одобрена администратором.
     */
    APPROVED("Заявка одобрена"),

    /**
     * Заявка отклонена администратором.
     */
    REJECTED("Заявка отклонена");

    /**
     * Человекочитаемое описание статуса.
     */
    private final String description;

    CardRequestStatus(String description) {
        this.description = description;
    }
}
