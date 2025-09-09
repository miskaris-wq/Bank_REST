package com.example.bankcards.exception;

import com.example.bankcards.exception.api.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
