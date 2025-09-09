package com.example.bankcards.mappers;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BankCardMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "id", target = "cardId")
    BankCardDTO toDto(BankCard card);
}
