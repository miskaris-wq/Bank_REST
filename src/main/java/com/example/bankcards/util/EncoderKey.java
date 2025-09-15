package com.example.bankcards.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Утилита для восстановления AES-ключа из строки Base64.
 * <p>
 * Поддерживаются ключи длиной 16, 24 или 32 байта (AES-128, AES-192, AES-256).
 * </p>
 *
 * Пример:
 * <pre>{@code
 * String base64Key = "mZNhZtXwLTxmAq8GJqEjEg==";
 * SecretKey key = EncoderKey.fromBase64ToAes(base64Key);
 * }</pre>
 *
 * @author ksenya
 */
public final class EncoderKey {

    private EncoderKey() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Декодирует Base64-строку в {@link SecretKey} для алгоритма AES.
     *
     * @param base64Key AES-ключ в формате Base64
     * @return объект {@link SecretKey}
     * @throws IllegalArgumentException если строка пустая или длина ключа некорректна
     */
    public static SecretKey fromBase64ToAes(String base64Key) {
        if (base64Key == null || base64Key.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key must not be null or empty");
        }

        byte[] keyBytes = Base64.getDecoder().decode(base64Key.trim());

        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalArgumentException(
                    String.format("Invalid key length: %d bytes (expected 16, 24 or 32)", keyBytes.length)
            );
        }

        return new SecretKeySpec(keyBytes, "AES");
    }
}
