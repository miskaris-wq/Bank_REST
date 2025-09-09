package com.example.bankcards.entity.card;

import lombok.Getter;

@Getter
public enum CardStatus {
    ACTIVE("Активна"),
    BLOCKED("Заблокирована"),
    EXPIRED("Истек срок");

    final String status;

    CardStatus(String status){
        this.status = status;
    }

}
