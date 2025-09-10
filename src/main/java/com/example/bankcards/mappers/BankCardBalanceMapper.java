package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankCardBalanceMapper {

    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDTO toDto(BankCard card);

    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDTO toDto(BankCardDTO card);
}
