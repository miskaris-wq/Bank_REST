package com.example.bankcards.exception;

import com.example.bankcards.exception.api.ForbiddenException;

public class CardAccessDeniedException extends ForbiddenException {
    public CardAccessDeniedException(String message) {
        super(message);
    }
}
