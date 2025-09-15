package com.example.bankcards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Обработчик ошибок доступа (403 Forbidden) для REST API.
 * <p>
 * Используется Spring Security, чтобы возвращать клиенту JSON-ответ
 * вместо стандартной HTML-страницы при ошибке доступа.
 * </p>
 *
 * <p>Формат ответа JSON:</p>
 * <pre>
 * {
 *   "status": 403,
 *   "error": "Access Denied",
 *   "message": "Причина отказа",
 *   "path": "/api/v1/..."
 * }
 * </pre>
 */
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Обрабатывает ситуацию, когда доступ к ресурсу запрещён.
     *
     * @param request  HTTP-запрос
     * @param response HTTP-ответ
     * @param ex       исключение {@link AccessDeniedException}, вызвавшее отказ
     * @throws IOException      если возникает ошибка записи в поток ответа
     * @throws ServletException если возникает ошибка в обработке запроса
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        log.error(ex.getMessage());

        Map<String, Object> body = Map.of(
                "status", HttpServletResponse.SC_FORBIDDEN,
                "error", "Access Denied",
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
