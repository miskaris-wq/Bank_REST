package com.example.bankcards.exception;

import com.example.bankcards.exception.api.BadRequestException;

public class MissingCredentialsException extends BadRequestException {
    public MissingCredentialsException(String message) {
        super(message);
    }
}
