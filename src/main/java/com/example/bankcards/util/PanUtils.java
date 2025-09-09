package com.example.bankcards.util;

public final class PanUtils {
    private PanUtils() {}

    /** Проверка алгоритмом Луна. Работает по строке, содержащей только цифры. */
    public static boolean isLuhnValid(String digitsOnlyPan) {
        if (digitsOnlyPan == null || !digitsOnlyPan.matches("\\d{12,19}")) return false;

        int sum = 0;
        boolean dbl = false;
        for (int i = digitsOnlyPan.length() - 1; i >= 0; i--) {
            int d = digitsOnlyPan.charAt(i) - '0';
            if (dbl) {
                d = d * 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            dbl = !dbl;
        }
        return sum % 10 == 0;
    }

    /** Бросает IllegalArgumentException при невалидном PAN. */
    public static void requireValid(String panRaw) {
        String digits = MaskingUtils.normalizePan(panRaw);
        if (!isLuhnValid(digits)) {
            throw new IllegalArgumentException("Invalid card number");
        }
    }
}
