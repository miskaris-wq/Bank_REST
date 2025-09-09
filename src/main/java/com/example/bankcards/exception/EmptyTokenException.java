package com.example.bankcards.exception;

import com.example.bankcards.exception.api.BadRequestException;

public class EmptyTokenException extends BadRequestException {
    public EmptyTokenException(String message) {
        super(message);
    }
}
