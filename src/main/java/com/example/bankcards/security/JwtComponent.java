package com.example.bankcards.security;

import com.example.bankcards.exception.EmptyTokenException;
import com.example.bankcards.exception.InvalidTokenException;
import com.example.bankcards.exception.TokenExpiredException;
import com.example.bankcards.util.DecoderKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Компонент для работы с JWT-токенами.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Генерацию токенов на основе имени пользователя</li>
 *     <li>Извлечение claims (subject, expiration и др.)</li>
 *     <li>Валидацию токена (срок действия, соответствие пользователю)</li>
 * </ul>
 * </p>
 */
@Component
@Slf4j
public class JwtComponent {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    /**
     * Декодирует секретный ключ из Base64 после инициализации бина.
     */
    @PostConstruct
    private void decryptedKey() {
        key = DecoderKey.fromBase64(jwtSecret).getSecretKey();
    }

    /**
     * Генерация JWT-токена для указанного пользователя.
     *
     * @param username имя пользователя
     * @return сгенерированный токен
     */
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(key)
                .compact();
    }

    /**
     * Извлекает имя пользователя (subject) из токена.
     *
     * @param token JWT-токен
     * @return имя пользователя
     * @throws EmptyTokenException если токен пустой
     */
    public String extractUserName(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает дату истечения токена.
     *
     * @param token JWT-токен
     * @return дата истечения
     * @throws EmptyTokenException если токен пустой
     */
    public Date extractExpiration(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Проверяет, что токен не пустой.
     *
     * @param token JWT-токен
     * @throws EmptyTokenException если токен пустой
     */
    public void checkTokenOnEmpty(String token) {
        if (token == null || token.isBlank()) {
            throw new EmptyTokenException("Токен пустой");
        }
    }

    /**
     * Универсальный метод для извлечения любого claim.
     *
     * @param token           JWT-токен
     * @param claimsResolvers функция для получения конкретного claim
     * @param <T>             тип возвращаемого значения
     * @return значение claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Извлекает все claims из токена.
     *
     * @param token JWT-токен
     * @return объект {@link Claims}
     * @throws TokenExpiredException если срок действия токена истёк
     * @throws InvalidTokenException если токен повреждён или некорректен
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Срок действия токена истёк. Пожалуйста, авторизуйтесь заново");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new InvalidTokenException("Недействительный токен");
        }
    }

    /**
     * Проверяет валидность токена:
     * <ul>
     *     <li>Имя пользователя из токена совпадает с {@link UserDetails}</li>
     *     <li>Срок действия токена не истёк</li>
     * </ul>
     *
     * @param token       JWT-токен
     * @param userDetails пользовательские данные
     * @return {@code true}, если токен валиден
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractUserName(token);
        return login.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истёк ли срок действия токена.
     *
     * @param token JWT-токен
     * @return {@code true}, если токен истёк
     */
    protected boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
