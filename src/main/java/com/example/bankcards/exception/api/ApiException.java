package com.example.bankcards.exception.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Getter
public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;

    protected ApiException(String message, HttpStatus status) {
        super(Objects.requireNonNull(message));
        this.status = Objects.requireNonNull(status);
    }
}