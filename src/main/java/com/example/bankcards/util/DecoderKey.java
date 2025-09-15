package com.example.bankcards.util;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Утилита для декодирования симметричного ключа из строки Base64.
 * <p>
 * Используется для восстановления {@link SecretKey},
 * применяемого в алгоритмах HMAC (например, при работе с JWT).
 * </p>
 *
 * Пример:
 * <pre>{@code
 * String base64 = "c2VjcmV0S2V5U3RyaW5n...";
 * DecoderKey decoderKey = DecoderKey.fromBase64(base64);
 * SecretKey key = decoderKey.getSecretKey();
 * }</pre>
 *
 * @author ksenya
 */
@Getter
public final class DecoderKey {

    private final SecretKey secretKey;

    private DecoderKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Декодирует Base64-представление секретного ключа и создает {@link DecoderKey}.
     *
     * @param base64Key ключ в формате Base64
     * @return объект {@link DecoderKey} с готовым {@link SecretKey}
     * @throws IllegalArgumentException если строка пустая или невалидна для Base64
     */
    public static DecoderKey fromBase64(String base64Key) {
        String trimmed = base64Key.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Encoded key must not be empty");
        }

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(trimmed);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Failed to Base64-decode the key", ex);
        }

        SecretKey key = Keys.hmacShaKeyFor(decoded);
        return new DecoderKey(key);
    }
}
