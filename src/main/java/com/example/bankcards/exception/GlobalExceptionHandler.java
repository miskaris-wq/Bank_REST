package com.example.bankcards.exception;

import com.example.bankcards.dto.response.APIResponse;
import com.example.bankcards.exception.api.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * <p>
 * Перехватывает исключения, возникающие в приложении,
 * и возвращает унифицированный {@link APIResponse} с описанием ошибки.
 * </p>
 *
 * Поддерживаются следующие типы ошибок:
 * <ul>
 *     <li>{@link ApiException} — кастомные исключения бизнес-логики;</li>
 *     <li>{@link AccessDeniedException} — ошибки доступа (HTTP 403 Forbidden).</li>
 * </ul>
 *
 * @author ksenya
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка бизнес-исключений ({@link ApiException}).
     *
     * @param ex выброшенное исключение
     * @return ответ с кодом из {@link ApiException#getStatus()} и сообщением об ошибке
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<APIResponse<Void>> handleApiException(ApiException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(APIResponse.ofError(ex.getMessage(), ex.getStatus()));
    }

    /**
     * Обработка ошибок доступа ({@link AccessDeniedException}).
     *
     * @param ex выброшенное исключение
     * @return ответ с кодом {@link HttpStatus#FORBIDDEN} (403) и сообщением об ошибке
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(APIResponse.ofError(ex.getMessage(), HttpStatus.FORBIDDEN));
    }
}
