package com.example.bankcards.entity.block;

import lombok.Getter;

@Getter
public enum CardRequestStatus {
    PENDING("Заявка ожидает рассмотрения"),
    APPROVED("Заявка одобрена"),
    REJECTED("Заявка отклонена");

    private final String description;

    CardRequestStatus(String description) {
        this.description = description;
    }

}

