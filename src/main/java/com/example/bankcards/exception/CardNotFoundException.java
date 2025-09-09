package com.example.bankcards.exception;

import com.example.bankcards.exception.api.NotFoundException;

public class CardNotFoundException extends NotFoundException {

    public CardNotFoundException(String message) {
        super(message);
    }
}