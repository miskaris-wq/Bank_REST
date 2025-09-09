package com.example.bankcards.exception;

import com.example.bankcards.exception.api.UnauthorizedException;

public class JwtAuthenticationException extends UnauthorizedException {
    public JwtAuthenticationException(String msg) { super(msg); }
}