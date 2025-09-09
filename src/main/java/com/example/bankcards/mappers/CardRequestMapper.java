package com.example.bankcards.mappers;

import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.entity.block.CardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardRequestMapper {

    @Mapping(source = "initiator.id", target = "initiatorId")
    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "id", target = "requestId")
    CardRequestDTO toDto(CardRequest blockRequest);
}
