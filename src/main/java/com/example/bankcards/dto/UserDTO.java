package com.example.bankcards.dto;

import com.example.bankcards.entity.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Set<Role> roles;
}
