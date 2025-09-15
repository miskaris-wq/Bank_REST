package com.example.bankcards.entity.bankcard;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("Активна"),
    BLOCKED("Заблокирована"),
    EXPIRED("Истек срок");

    final String newStatus;

    Status(String newStatus){
        this.newStatus = newStatus;
    }

    @Override
    public String toString() {
        return newStatus;
    }
}
