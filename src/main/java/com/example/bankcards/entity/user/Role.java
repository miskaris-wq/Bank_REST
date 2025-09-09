package com.example.bankcards.entity.user;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }



    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
