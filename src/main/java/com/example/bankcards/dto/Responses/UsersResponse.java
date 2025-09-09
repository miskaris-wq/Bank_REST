package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class UsersResponse extends Response<Page<UserDTO>>{
    public UsersResponse(Page<UserDTO> data, String message, HttpStatus status) {
        super(data, message, status);
    }
}
