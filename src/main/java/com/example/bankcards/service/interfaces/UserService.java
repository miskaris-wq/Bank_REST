package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User getUserByUsername(String username);

    User getCurrentUser();

    Page<UserDTO> getUserByUsername(int pageNumber, int pageSize);

    void delete(Long id);

    UserDTO getUserById(Long id);

    TotalCardBalanceDTO getTotalBalance(Long userId);

    UserDTO update(Long id, UserDTO userDTO);
}
