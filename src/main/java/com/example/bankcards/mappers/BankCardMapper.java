package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для преобразования сущности {@link BankCard} в {@link BankCardDTO}.
 * <p>
 * Используется {@link org.mapstruct.MapStruct} для автоматической генерации
 * реализации маппинга.
 * </p>
 *
 * <ul>
 *     <li>{@code owner.id → ownerId}</li>
 *     <li>{@code id → cardId}</li>
 * </ul>
 *
 * Остальные поля маппятся автоматически по совпадению названий.
 *
 * @author ksenya
 */
@Mapper(componentModel = "spring")
public interface BankCardMapper {

    /**
     * Конвертирует {@link BankCard} в {@link BankCardDTO}.
     *
     * @param card сущность банковской карты
     * @return DTO банковской карты
     */
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "id", target = "cardId")
    BankCardDTO toDto(BankCard card);
}
