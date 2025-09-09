package com.example.bankcards.entity.user;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    final String roleName;

    Role(String roleName) {

        this.roleName = roleName;
    }
}
