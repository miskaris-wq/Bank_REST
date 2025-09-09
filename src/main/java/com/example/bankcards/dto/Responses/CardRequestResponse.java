package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.CardRequestDTO;
import org.springframework.http.HttpStatus;

public class CardRequestResponse extends Response<CardRequestDTO>{
    public CardRequestResponse(CardRequestDTO blockRequest, String message, HttpStatus status) {
        super(blockRequest, message, status);
    }
}
