package com.example.bankcards.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncoderKey {

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