package com.example.bankcards.exception;

import com.example.bankcards.exception.api.NotFoundException;

public class CustomUserNotFoundException extends NotFoundException {

    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
