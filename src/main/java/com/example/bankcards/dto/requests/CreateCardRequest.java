package com.example.bankcards.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на создание новой банковской карты.
 *
 * <p>Используется при вызове метода контроллера для добавления карты
 * конкретному пользователю.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequest {

    /**
     * Идентификатор пользователя, которому будет принадлежать карта.
     */
    private Long ownerId;
}
