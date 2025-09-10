package com.example.bankcards.controller;

import com.example.bankcards.controller.impl.UserControllerImpl;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    private MockMvc mvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserControllerImpl userController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private static String toJson(Object obj) throws Exception {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }

    @Test
    @DisplayName("GET /api/v1/user/all → 200 OK + list of users")
    void getAll_Success() throws Exception {
        UserDTO u = new UserDTO();
        u.setUsername("user1");
        u.setPassword("pass1");
        u.setRoles(Set.of(Role.USER));
        Page<UserDTO> page = new PageImpl<>(List.of(u), PageRequest.of(1, 2), 1);
        given(userServiceImpl.getUserByUsername(1, 2)).willReturn(page);

        mvc.perform(get("/api/v1/user/all").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].username").value("user1"))
                .andExpect(jsonPath("$.data.content[0].password").value("pass1"))
                .andExpect(jsonPath("$.data.content[0].roles").value("USER"))
                .andExpect(jsonPath("$.message").value("Пользователи успешно возвращены"));
    }

    @Test
    @DisplayName("GET /api/v1/user/{id} → 200 OK + single user")
    void getId_Success() throws Exception {
        UserDTO u = new UserDTO();
        u.setUsername("user2");
        u.setPassword("pass2");
        u.setRoles(Set.of(Role.ADMIN));
        given(userServiceImpl.getUserById(5L)).willReturn(u);

        mvc.perform(get("/api/v1/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("user2"))
                .andExpect(jsonPath("$.data.password").value("pass2"))
                .andExpect(jsonPath("$.data.roles").value("ADMIN"))
                .andExpect(jsonPath("$.message").value("Пользователь успешно возвращен"));
    }

    @Test
    @DisplayName("PATCH /api/v1/user/{id} → 200 OK + updated user")
    void update_Success() throws Exception {
        UserDTO u = new UserDTO();
        u.setUsername("updatedUser");
        u.setPassword("newPass");
        u.setRoles(Set.of(Role.USER));
        given(userServiceImpl.update(5L, u)).willReturn(u);

        mvc.perform(patch("/api/v1/user/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("updatedUser"))
                .andExpect(jsonPath("$.data.password").value("newPass"))
                .andExpect(jsonPath("$.data.roles").value("USER"))
                .andExpect(jsonPath("$.message").value("Пользователь успешно обновлён"));
    }

    @Test
    @DisplayName("DELETE /api/v1/user/{id} → 200 OK + deletion message")
    void deleteUser_Success() throws Exception {
        mvc.perform(delete("/api/v1/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Пользователь успешно удалён"));
    }

    @Test
    @DisplayName("GET /api/v1/user/{userId}/total-balance → 200 OK + total balance")
    void getTotalBalance_Success() throws Exception {
        CardBalanceDTO cbd = CardBalanceDTO.builder().balance(BigDecimal.valueOf(100)).cardId(1L).build();
        TotalCardBalanceDTO total = TotalCardBalanceDTO.builder()
                .userId(5L)
                .cardBalances(List.of(cbd))
                .totalBalance(BigDecimal.valueOf(100))
                .build();
        given(userServiceImpl.getTotalBalance(5L)).willReturn(total);

        mvc.perform(get("/api/v1/user/5/total-balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(5))
                .andExpect(jsonPath("$.data.totalBalance").value(100))
                .andExpect(jsonPath("$.data.cardBalances[0].cardId").value(1))
                .andExpect(jsonPath("$.data.cardBalances[0].balance").value(100))
                .andExpect(jsonPath("$.message").value("Общий баланс пользователя получен"));
    }
}
