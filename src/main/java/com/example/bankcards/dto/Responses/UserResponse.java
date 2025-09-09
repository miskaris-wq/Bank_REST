package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.UserDTO;
import org.springframework.http.HttpStatus;

public class UserResponse extends Response<UserDTO>{
    public UserResponse(UserDTO user, String message, HttpStatus status) {
        super(user, message, status);
    }
}
