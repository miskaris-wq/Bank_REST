package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.CardBalanceDto;
import org.springframework.http.HttpStatus;

public class BalanceResponse extends Response<CardBalanceDto>{

    public BalanceResponse(CardBalanceDto balance, String message, HttpStatus status) {
        super(balance, message, status);
    }
}
