package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.TransferUserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class TransfersResponse extends Response<Page<TransferUserDto>>{
    public TransfersResponse(Page<TransferUserDto> transfers, String message, HttpStatus status) {
        super(transfers, message, status);
    }
}
