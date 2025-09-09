package com.example.bankcards.entity.bankcard;

import lombok.Getter;

public enum Status {
    ACTIVE("Активна"),
    BLOCKED("Заблокирована"),
    EXPIRED("Истек срок");

    @Getter
    final String status;

    Status(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                '}';
    }
}
