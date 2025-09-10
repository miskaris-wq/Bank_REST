package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.CardBalanceDTO;
import org.springframework.http.HttpStatus;

public class BalanceResponse extends Response<CardBalanceDTO>{

    public BalanceResponse(CardBalanceDTO balance, String message, HttpStatus status) {
        super(balance, message, status);
    }
}
