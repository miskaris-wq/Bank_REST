package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.TransferUserDto;
import com.example.bankcards.entity.transfer.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для преобразования сущности {@link Transfer} в {@link TransferUserDto}.
 * <p>
 * Используется {@link org.mapstruct.MapStruct} для автоматической генерации
 * реализации маппинга.
 * Подключает {@link BankCardMapper} для преобразования вложенных карт
 * ({@code fromCard}, {@code toCard}).
 * </p>
 *
 * <ul>
 *     <li>{@code id → transactionId}</li>
 *     <li>{@code initiator.id → initiatorId}</li>
 *     <li>{@code fromCard → fromCard} (через {@link BankCardMapper})</li>
 *     <li>{@code toCard → toCard} (через {@link BankCardMapper})</li>
 * </ul>
 *
 * Остальные поля маппятся автоматически по совпадению названий.
 *
 * @author ksenya
 */
@Mapper(componentModel = "spring", uses = {BankCardMapper.class})
public interface TransferMapper {

    /**
     * Конвертирует {@link Transfer} в {@link TransferUserDto}.
     *
     * @param transfer сущность перевода
     * @return DTO перевода
     */
    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "initiator.id", target = "initiatorId")
    @Mapping(source = "fromCard", target = "fromCard")
    @Mapping(source = "toCard", target = "toCard")
    TransferUserDto toDto(Transfer transfer);
}
