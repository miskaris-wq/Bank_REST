package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.UserController;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.TotalBalanceResponse;
import com.example.bankcards.dto.Responses.UserResponse;
import com.example.bankcards.dto.Responses.UsersResponse;
import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user") // ← ДОБАВЬТЕ ЭТУ АННОТАЦИЮ
@RequiredArgsConstructor // ← Для инъекции зависимостей
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UsersResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        Page<UserDTO> usersDTO = userService.getUserByUsername(pageNumber, pageSize);
        return ResponseEntity.ok().body(new UsersResponse(usersDTO, "Пользователи успешно возвращены", HttpStatus.OK));
    }

    @Override
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponse> getId(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(new UserResponse(userDTO, "Пользователь успешно возвращен", HttpStatus.OK));
    }

    @Override
    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponse> update(
            @PathVariable(name = "id") Long id,
            @RequestBody UserDTO userDTO) {
        userDTO = userService.update(id, userDTO);
        return ResponseEntity.ok().body(new UserResponse(userDTO, "Пользователь успешно обновлён", HttpStatus.OK));
    }

    @Override
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body(Response.of("Пользователь успешно удалён", HttpStatus.OK));
    }

    @Override
    @GetMapping("/{userId}/total-balance")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TotalBalanceResponse> getTotalBalance(@PathVariable Long userId) {
        TotalCardBalanceDTO totalCardBalanceDTO = userService.getTotalBalance(userId);
        return ResponseEntity.ok().body(new TotalBalanceResponse(totalCardBalanceDTO, "Общий баланс пользователя получен", HttpStatus.OK));
    }
}