package com.example.bankcards.util;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.util.Base64;

@Getter
public final class DecoderKey {

    public SecretKey secretKey;

    private DecoderKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

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
