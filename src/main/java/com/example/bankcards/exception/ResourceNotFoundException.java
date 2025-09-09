package com.example.bankcards.exception;

import com.example.bankcards.exception.api.NotFoundException;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
