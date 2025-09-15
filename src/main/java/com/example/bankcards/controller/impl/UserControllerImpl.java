package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.interfaces.UserController;
import com.example.bankcards.dto.payload.TotalCardBalanceDTO;
import com.example.bankcards.dto.payload.UserDTO;
import com.example.bankcards.dto.response.APIResponse;
import com.example.bankcards.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userServiceImpl;


    @Override
    public ResponseEntity<APIResponse<Page<UserDTO>>> getAll(int pageNumber, int pageSize) {
        Page<UserDTO> users = userServiceImpl.getUserByUsername(pageNumber, pageSize);
        return ResponseEntity.ok(APIResponse.ofSuccess(users, "Пользователи успешно возвращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<UserDTO>> getId(Long id) {
        UserDTO userDTO = userServiceImpl.getUserById(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(userDTO, "Пользователь успешно возвращён", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<UserDTO>> update(Long id, UserDTO userDTO) {
        UserDTO updated = userServiceImpl.update(id, userDTO);
        return ResponseEntity.ok(APIResponse.ofSuccess(updated, "Пользователь успешно обновлён", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> deleteUser(Long id) {
        userServiceImpl.delete(id);
        return ResponseEntity.ok(APIResponse.ofSuccess(null, "Пользователь успешно удалён", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<APIResponse<TotalCardBalanceDTO>> getTotalBalance(Long userId) {
        TotalCardBalanceDTO totalCardBalanceDTO = userServiceImpl.getTotalBalance(userId);
        return ResponseEntity.ok(APIResponse.ofSuccess(totalCardBalanceDTO, "Общий баланс пользователя получен", HttpStatus.OK));
    }
}
