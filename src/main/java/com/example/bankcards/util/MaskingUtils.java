package com.example.bankcards.util;

public final class MaskingUtils {
    private MaskingUtils() {}

    /** Оставляет только цифры из PAN. */
    public static String normalizePan(String pan) {
        return pan == null ? null : pan.replaceAll("\\D", "");
    }

    /** Последние 4 цифры PAN. */
    public static String last4(String pan) {
        String d = normalizePan(pan);
        if (d == null || d.length() < 4) return d;
        return d.substring(d.length() - 4);
    }

    /** Маска для отображения пользователю. */
    public static String maskPan(String pan) {
        String l4 = last4(pan);
        return "**** **** **** " + (l4 == null ? "" : l4);
    }
}
