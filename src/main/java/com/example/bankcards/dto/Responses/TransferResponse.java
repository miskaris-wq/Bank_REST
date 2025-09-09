package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.TransferUserDto;
import org.springframework.http.HttpStatus;

public class TransferResponse extends Response<TransferUserDto> {
    public TransferResponse(TransferUserDto data, String message, HttpStatus status) {
        super(data, message, status);
    }
}
