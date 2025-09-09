package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.BankCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class CardsResponse extends Response<Page<BankCardDTO>> {

    public CardsResponse(Page<BankCardDTO> data, String message, HttpStatus status) {
        super(data, message, status);
    }
}
