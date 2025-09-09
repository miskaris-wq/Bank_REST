package com.example.bankcards.util;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public final class ExpiryUtils {
    private ExpiryUtils() {}

    /** Парсит YYYY-MM, бросает IllegalArgumentException при ошибке. */
    public static YearMonth parse(String expiry) {
        try {
            return YearMonth.parse(expiry);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expiry must be in YYYY-MM format");
        }
    }

    /** Истёк ли срок на текущую дату. */
    public static boolean isExpired(YearMonth ym) {
        return ym.isBefore(YearMonth.now());
    }

    /** Требует, чтобы карта не была просрочена. */
    public static void requireNotExpired(YearMonth ym) {
        if (isExpired(ym)) {
            throw new IllegalStateException("Card is expired");
        }
    }
}
