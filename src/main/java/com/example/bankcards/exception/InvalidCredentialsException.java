package com.example.bankcards.exception;

import com.example.bankcards.exception.api.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
