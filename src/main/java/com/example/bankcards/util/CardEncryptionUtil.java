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

/**
 * Утилита для шифрования, дешифрования и маскировки номеров банковских карт.
 * <p>
 * Использует алгоритм AES в режиме CBC с паддингом PKCS5.
 * Хранит ключ шифрования, заданный в настройках приложения ({@code card.secretKey}),
 * и выполняет операции с безопасной генерацией вектора инициализации (IV).
 * </p>
 *
 * <ul>
 *   <li>{@link #encrypt(String)} — шифрует номер карты и возвращает строку в Base64.</li>
 *   <li>{@link #decrypt(String)} — дешифрует ранее зашифрованный номер карты.</li>
 *   <li>{@link #maskRaw(String)} — маскирует "сырой" номер карты, оставляя только последние 4 цифры.</li>
 *   <li>{@link #maskEncrypted(String)} — дешифрует и маскирует зашифрованный номер карты.</li>
 * </ul>
 *
 * Пример маскировки: {@code 1234 5678 9012 3456 -> **** **** **** 3456}.
 *
 * @author ksenya
 */
@Component
public final class CardEncryptionUtil {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH_BYTES = 16;
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Конструктор инициализирует утилиту ключом из конфигурации.
     *
     * @param base64Key секретный ключ в формате Base64 (из application.yml/properties).
     * @throws IllegalArgumentException если ключ пустой или не задан
     */
    public CardEncryptionUtil(@Value("${card.secretKey}") String base64Key) {
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalArgumentException("Секретный ключ карты не задан. Укажите card.secretKey в настройках приложения.");
        }

        String trimmed = base64Key.trim();
        this.secretKey = EncoderKey.fromBase64ToAes(trimmed);
    }

    /**
     * Маскирует открытый номер карты, скрывая все цифры кроме последних четырёх.
     *
     * @param rawCardNumber открытый номер карты
     * @return маскированный номер вида {@code **** **** **** 1234}
     */
    public String maskRaw(String rawCardNumber) {
        checkIsEmpty(rawCardNumber);

        String digits = rawCardNumber.replaceAll("\\s+", "");
        if (digits.length() < 4) {
            throw new IllegalArgumentException("Неккоретный номер карты. Должно быть минимум 4 цифры");
        }
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

    /**
     * Дешифрует и маскирует номер карты.
     *
     * @param encryptedCardNumber зашифрованный номер карты (Base64)
     * @return маскированный номер
     */
    public String maskEncrypted(String encryptedCardNumber) {
        String raw = decrypt(encryptedCardNumber);
        return maskRaw(raw);
    }

    /**
     * Шифрует открытый номер карты.
     *
     * @param rawCardNumber номер карты в открытом виде
     * @return зашифрованный номер в Base64
     */
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

    /**
     * Дешифрует номер карты.
     *
     * @param encryptedCardNumber зашифрованный номер карты (Base64)
     * @return расшифрованный номер карты
     */
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

    /**
     * Проверяет, что строка не пустая и не null.
     *
     * @param cardNumber номер карты
     * @throws IllegalArgumentException если строка пустая
     */
    private void checkIsEmpty(String cardNumber){
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Номер карты не должен быть пустым");
        }
    }

}

