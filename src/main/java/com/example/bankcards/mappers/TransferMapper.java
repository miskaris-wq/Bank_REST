package com.example.bankcards.mappers;

import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.transfer.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankCardMapper.class})
public interface TransferMapper {

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "initiator.id", target = "initiatorId")
    @Mapping(source = "fromCard", target = "fromCard")
    @Mapping(source = "toCard",   target = "toCard")
    TransferUserDto toDto(Transfer transfer);
}
