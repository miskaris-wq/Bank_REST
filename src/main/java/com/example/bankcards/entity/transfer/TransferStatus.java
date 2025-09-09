package com.example.bankcards.entity.transfer;

public enum TransferStatus {
    COMPLETED("Завершена"),
    PROCESS("В процессе"),
    CANCELLED("Отменена");

    final String status;

    TransferStatus(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                '}';
    }
}
