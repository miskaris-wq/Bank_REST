package com.example.bankcards.entity.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AesGcmEncryptor {

    private final SecretKey key;

    public AesGcmEncryptor(@Value("${app.crypto.aes-key}") String keyStr) {
        this.key = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String plain) {
        try {
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(12);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
            byte[] enc = c.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            byte[] all = new byte[iv.length + enc.length];
            System.arraycopy(iv, 0, all, 0, iv.length);
            System.arraycopy(enc, 0, all, iv.length, enc.length);
            return Base64.getEncoder().encodeToString(all);
        } catch (Exception e) {
            throw new IllegalStateException("Encrypt error", e);
        }
    }

    public String decrypt(String cipherB64) {
        try {
            byte[] all = Base64.getDecoder().decode(cipherB64);
            byte[] iv = Arrays.copyOfRange(all, 0, 12);
            byte[] enc = Arrays.copyOfRange(all, 12, all.length);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            return new String(c.doFinal(enc), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Decrypt error", e);
        }
    }
}
