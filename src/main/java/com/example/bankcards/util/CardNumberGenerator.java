package com.example.bankcards.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {

    private static final int CARD_NUMBER_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCardNumber() {

        int[] digits = new int[CARD_NUMBER_LENGTH];
        for (int i = 0; i < CARD_NUMBER_LENGTH - 1; i++) {
            digits[i] = RANDOM.nextInt(10);
        }

        digits[CARD_NUMBER_LENGTH - 1] = calculateLuhnCheckDigit(digits);

        StringBuilder sb = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int digit : digits) {
            sb.append(digit);
        }
        return sb.toString();
    }

    private static int calculateLuhnCheckDigit(int[] digits) {
        int sum = 0;
        boolean doubleIt = true;

        for (int i = CARD_NUMBER_LENGTH - 2; i >= 0; i--) {
            int d = digits[i];
            if (doubleIt) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleIt = !doubleIt;
        }

        return (10 - (sum % 10)) % 10;
    }

}
