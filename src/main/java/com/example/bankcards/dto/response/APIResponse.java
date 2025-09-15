package com.example.bankcards.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Универсальный класс-обёртка для API-ответов.
 * <p>
 * Используется во всех контроллерах для стандартизации структуры ответа:
 * - всегда возвращается информация об успехе операции;
 * - код статуса HTTP;
 * - сообщение для пользователя;
 * - полезная нагрузка (data).
 * </p>
 *
 * @param <T> тип полезных данных, возвращаемых в ответе
 * @author ksenya
 */
@Getter
public class APIResponse<T> {

    /**
     * Флаг успеха операции (true, если HTTP-статус 2xx).
     */
    private final boolean success;

    /**
     * Сообщение, сопровождающее ответ (успех/ошибка).
     */
    private final String message;

    /**
     * Полезные данные ответа (могут быть null в случае ошибки).
     */
    private final T data;

    /**
     * HTTP-статус ответа в числовом виде.
     */
    private final int status;

    /**
     * Конструктор для создания ответа.
     *
     * @param data   полезные данные (может быть {@code null})
     * @param message сообщение для клиента
     * @param status HTTP-статус
     */
    public APIResponse(T data, String message, HttpStatus status) {
        this.success = status.is2xxSuccessful();
        this.message = message;
        this.data = data;
        this.status = status.value();
    }

    /**
     * Создаёт успешный ответ.
     *
     * @param data   полезные данные
     * @param message сообщение
     * @param status HTTP-статус (должен быть из 2xx)
     * @return экземпляр {@link APIResponse}
     */
    public static <T> APIResponse<T> ofSuccess(T data, String message, HttpStatus status) {
        return new APIResponse<>(data, message, status);
    }

    /**
     * Создаёт ответ с ошибкой.
     *
     * @param message сообщение об ошибке
     * @param status HTTP-статус (обычно 4xx или 5xx)
     * @return экземпляр {@link APIResponse} с {@code null} в поле data
     */
    public static <T> APIResponse<T> ofError(String message, HttpStatus status) {
        return new APIResponse<>(null, message, status);
    }
}
