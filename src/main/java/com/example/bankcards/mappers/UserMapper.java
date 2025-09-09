package com.example.bankcards.mappers;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    UserDTO toDto(User user);
}
