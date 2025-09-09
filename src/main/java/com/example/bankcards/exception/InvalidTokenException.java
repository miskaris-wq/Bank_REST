package com.example.bankcards.exception;

import com.example.bankcards.exception.api.BadRequestException;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
