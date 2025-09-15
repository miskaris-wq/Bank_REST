package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.BankCardDTO;
import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для преобразования сущностей {@link BankCard} и DTO {@link BankCardDTO}
 * в {@link CardBalanceDTO}.
 * <p>
 * Используется {@link org.mapstruct.MapStruct} для автоматической генерации
 * реализации маппинга.
 * </p>
 *
 * <ul>
 *     <li>Из {@link BankCard} в {@link CardBalanceDTO} маппятся:
 *         <ul>
 *             <li>{@code id → cardId}</li>
 *             <li>{@code cardNumber → maskedCardNumber}</li>
 *         </ul>
 *     </li>
 *     <li>Из {@link BankCardDTO} в {@link CardBalanceDTO} маппится:
 *         <ul>
 *             <li>{@code cardNumber → maskedCardNumber}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author ksenya
 */
@Mapper(componentModel = "spring")
public interface BankCardBalanceMapper {

    /**
     * Конвертирует {@link BankCard} в {@link CardBalanceDTO}.
     *
     * @param card сущность {@link BankCard}
     * @return DTO с балансом карты
     */
    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDTO toDto(BankCard card);

    /**
     * Конвертирует {@link BankCardDTO} в {@link CardBalanceDTO}.
     *
     * @param card DTO банковской карты
     * @return DTO с балансом карты
     */
    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDTO toDto(BankCardDTO card);
}
