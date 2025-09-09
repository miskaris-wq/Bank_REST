package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.JwtDTO;
import org.springframework.http.HttpStatus;

public class JwtResponse extends Response<JwtDTO> {

    public JwtResponse(JwtDTO token, String message, HttpStatus status) {
        super(token, message, status);
    }

}