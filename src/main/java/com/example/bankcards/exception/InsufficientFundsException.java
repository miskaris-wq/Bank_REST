package com.example.bankcards.exception;


import com.example.bankcards.exception.api.BadRequestException;

public class InsufficientFundsException extends BadRequestException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
