package com.example.bankcards.exception;

import com.example.bankcards.exception.api.NotFoundException;

public class TransferNotFoundException extends NotFoundException {
    public TransferNotFoundException(String message) {
        super(message);
    }
}
