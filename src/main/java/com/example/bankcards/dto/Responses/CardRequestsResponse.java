package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.CardRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class CardRequestsResponse extends Response<Page<CardRequestDTO>>{
    public CardRequestsResponse(Page<CardRequestDTO> cardRequests, String message, HttpStatus status) {
        super(cardRequests, message, status);
    }
}
