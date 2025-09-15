package com.example.bankcards.mappers;

import com.example.bankcards.dto.payload.UserDTO;
import com.example.bankcards.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для преобразования сущности {@link User} в {@link UserDTO}.
 * <p>
 * Используется {@link org.mapstruct.MapStruct} для автоматической генерации
 * реализации маппинга.
 * </p>
 *
 * <ul>
 *     <li>{@code roles → roles}</li>
 * </ul>
 *
 * Остальные поля маппятся автоматически по совпадению названий.
 *
 * @author ksenya
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Конвертирует {@link User} в {@link UserDTO}.
     *
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(source = "roles", target = "roles")
    UserDTO toDto(User user);
}
