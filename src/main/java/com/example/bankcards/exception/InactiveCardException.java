package com.example.bankcards.exception;

import com.example.bankcards.exception.api.ConflictException;

public class InactiveCardException extends ConflictException {
    public InactiveCardException(String message) {
        super(message);
    }
}
