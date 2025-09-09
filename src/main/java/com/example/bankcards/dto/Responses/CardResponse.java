package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.BankCardDTO;
import org.springframework.http.HttpStatus;

public class CardResponse extends Response<BankCardDTO> {

    public CardResponse(BankCardDTO data, String message, HttpStatus status) {
        super(data, message, status);
    }
}
