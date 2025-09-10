package com.example.bankcards.dto.payload;

import com.example.bankcards.entity.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Set<Role> roles;
}
