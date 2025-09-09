package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public final class CardEncryptionUtil {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH_BYTES = 16;
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();


    public CardEncryptionUtil(@Value("${card.secretKey}") String base64Key) {
        String trimmed = (base64Key != null) ? base64Key.trim() : null;
        checkIsEmpty(trimmed);

        this.secretKey = EncoderKey.fromBase64ToAes(trimmed);
    }

    public String maskRaw(String rawCardNumber) {
        checkIsEmpty(rawCardNumber);

        String digits = rawCardNumber.replaceAll("\\s+", "");
        if (digits.length() < 4) {
            throw new IllegalArgumentException("Неккоретный номер карты. Должно быть минимум 4 цифры");
        }
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

    public String maskEncrypted(String encryptedCardNumber) {
        String raw = decrypt(encryptedCardNumber);
        return maskRaw(raw);
    }

    public String encrypt(String rawCardNumber) {
        checkIsEmpty(rawCardNumber);

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] ciphertext = cipher.doFinal(rawCardNumber.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return BASE64_ENCODER.encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка при шифровании номера карты", e);
        }
    }


    public String decrypt(String encryptedCardNumber) {
        checkIsEmpty(encryptedCardNumber);

        try {
            byte[] combined = BASE64_DECODER.decode(encryptedCardNumber);
            byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH_BYTES);
            byte[] ciphertext = Arrays.copyOfRange(combined, IV_LENGTH_BYTES, combined.length);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] raw = cipher.doFinal(ciphertext);
            return new String(raw, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка при дешифровании номера карты", e);
        }
    }

    private void checkIsEmpty(String CardNumber){
        if (CardNumber == null || CardNumber.isBlank()) {
            throw new IllegalArgumentException("Номер карты не должен быть пустым");
        }
    }

}

