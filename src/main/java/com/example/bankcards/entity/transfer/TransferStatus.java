package com.example.bankcards.entity.transfer;

/**
 * Перечисление, представляющее возможные статусы перевода между картами.
 * <p>
 * Используется для отслеживания прогресса и результата транзакции.
 * </p>
 *
 * <ul>
 *     <li>{@link #COMPLETED} — перевод успешно завершён;</li>
 *     <li>{@link #PROCESS} — перевод находится в процессе выполнения;</li>
 *     <li>{@link #CANCELLED} — перевод был отменён.</li>
 * </ul>
 *
 * @author ksenya
 */
public enum TransferStatus {

    /** Перевод успешно завершён. */
    COMPLETED("Завершена"),

    /** Перевод выполняется. */
    PROCESS("В процессе"),

    /** Перевод был отменён. */
    CANCELLED("Отменена");

    /** Человекочитаемое описание статуса. */
    private final String status;

    TransferStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
