package com.example.bankcards.exception;

import com.example.bankcards.exception.api.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
