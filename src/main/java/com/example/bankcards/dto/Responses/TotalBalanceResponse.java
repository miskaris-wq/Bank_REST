package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.TotalCardBalanceDTO;
import org.springframework.http.HttpStatus;

public class TotalBalanceResponse extends Response<TotalCardBalanceDTO>{

    public TotalBalanceResponse(TotalCardBalanceDTO totalBalance, String message, HttpStatus status) {
        super(totalBalance, message, status);
    }
}
