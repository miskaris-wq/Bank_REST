package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения BankCards.
 * <p>
 * Точка входа в Spring Boot-приложение. Запускает контекст Spring и инициализирует все необходимые бины.
 * </p>
 *
 * @author ksenya
 */
@SpringBootApplication
public class AppApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
