package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.CardRequestDTO;
import com.example.bankcards.entity.block.CardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для преобразования сущности {@link CardRequest} в {@link CardRequestDTO}.
 * <p>
 * Используется {@link org.mapstruct.MapStruct} для автоматической генерации
 * реализации маппинга.
 * </p>
 *
 * <ul>
 *     <li>{@code initiator.id → initiatorId}</li>
 *     <li>{@code card.id → cardId}</li>
 *     <li>{@code id → requestId}</li>
 * </ul>
 *
 * Остальные поля маппятся автоматически по совпадению названий.
 *
 * @author ksenya
 */
@Mapper(componentModel = "spring")
public interface CardRequestMapper {

    /**
     * Конвертирует {@link CardRequest} в {@link CardRequestDTO}.
     *
     * @param blockRequest сущность заявки на блокировку карты
     * @return DTO заявки
     */
    @Mapping(source = "initiator.id", target = "initiatorId")
    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "id", target = "requestId")
    CardRequestDTO toDto(CardRequest blockRequest);
}
