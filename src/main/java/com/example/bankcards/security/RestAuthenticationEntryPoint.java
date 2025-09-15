package com.example.bankcards.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Обработчик ошибок аутентификации (401 Unauthorized) для REST API.
 * <p>
 * Используется Spring Security для возврата JSON-ответа, когда пользователь
 * пытается получить доступ к защищённому ресурсу без аутентификации.
 * </p>
 *
 * <p>Формат ответа JSON:</p>
 * <pre>
 * {
 *   "error": "Необходима аутентификация",
 *   "message": "Подробное сообщение исключения"
 * }
 * </pre>
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Метод вызывается при отсутствии или недействительной аутентификации.
     *
     * @param req    входящий HTTP-запрос
     * @param resp   HTTP-ответ
     * @param authEx исключение {@link AuthenticationException}, вызвавшее ошибку
     * @throws IOException если возникает ошибка при записи ответа
     */
    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse resp,
                         AuthenticationException authEx)
            throws IOException {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setCharacterEncoding("UTF-8");

        String body = "{\"error\":\"Необходима аутентификация\",\"message\":\""
                + authEx.getMessage() + "\"}";
        resp.getWriter().write(body);
    }
}
