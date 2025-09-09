package com.example.bankcards.exception;

import com.example.bankcards.exception.api.ConflictException;

public class StatusAlreadySetException extends ConflictException {
    public StatusAlreadySetException(String message) {
        super(message);
    }
}
