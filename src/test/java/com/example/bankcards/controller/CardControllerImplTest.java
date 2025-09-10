package com.example.bankcards.controller;

import com.example.bankcards.controller.impl.CardControllerImpl;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.security.JwtComponent;
import com.example.bankcards.service.impl.CardServiceImpl;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardControllerImplTest {

    private MockMvc mvc;

    @Mock
    private CardServiceImpl cardServiceImpl;

    @InjectMocks
    private CardControllerImpl cardControllerImpl;

    @Mock
    private JwtComponent jwtComponent;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(cardControllerImpl).build();
    }

    private static String toJson(Object dto) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper()
                .findAndRegisterModules()
                .writeValueAsString(dto);
    }

    @Test
    @DisplayName("GET /api/v1/cards/all → 200 + список карт")
    void getAll_Success() throws Exception {
        BankCardDTO dto = BankCardDTO.builder().cardNumber("5555-6666-7777-8888").build();
        Page<BankCardDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0,5), 1);
        given(cardServiceImpl.getAll(0,5)).willReturn(page);

        mvc.perform(get("/api/v1/cards/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Все карты успешно возвращены"))
                .andExpect(jsonPath("$.data.content[0].cardNumber").value("5555-6666-7777-8888"));
    }

    @Test
    @DisplayName("GET /api/v1/cards/all/by-user/{id} → 200 + список карт пользователя")
    void getAllCurrentUser_Success() throws Exception {
        BankCardDTO dto = BankCardDTO.builder().cardNumber("1234-1234-1234-1234").build();
        Pageable pg = PageRequest.of(1,2);
        Page<BankCardDTO> page = new PageImpl<>(List.of(dto), pg, 1);
        given(cardServiceImpl.getAllCurrentUser(1,2, 99L)).willReturn(page);

        mvc.perform(get("/api/v1/cards/all/by-user/99")
                        .param("page","1").param("size","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карты пользователя получены"));
    }

    @Test
    @DisplayName("GET /api/v1/cards/{id} → 200 + одна карта")
    void getById_Success() throws Exception {
        BankCardDTO dto = BankCardDTO.builder().cardNumber("0000-0000-0000-0005").build();
        given(cardServiceImpl.getById(5L)).willReturn(dto);

        mvc.perform(get("/api/v1/cards/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Данные по карте найдены"));
    }

    @Test
    @DisplayName("POST /api/v1/cards/create → 200 + созданная карта")
    void create_Success() throws Exception {
        CreateCardRequest req = CreateCardRequest.builder().ownerId(10L).build();
        BankCardDTO dto = BankCardDTO.builder().cardNumber("9999 8888 7777 6666").build();
        given(cardServiceImpl.create(any(CreateCardRequest.class))).willReturn(dto);

        mvc.perform(post("/api/v1/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карта успешно создана"));
    }

    @Test
    @DisplayName("PATCH /api/v1/cards/blocked/{id} → 200 + заблокированная карта")
    void blocked_Success() throws Exception {
        BankCardDTO dto = BankCardDTO.builder().cardNumber("3333").build();
        given(cardServiceImpl.blocked(3L)).willReturn(dto);

        mvc.perform(patch("/api/v1/cards/blocked/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карта заблокирована"));
    }

    @Test
    @DisplayName("PATCH /api/v1/cards/activate/{id} → 200 + активированная карта")
    void activate_Success() throws Exception {
        BankCardDTO dto = BankCardDTO.builder().cardNumber("4444").build();
        given(cardServiceImpl.activate(4L)).willReturn(dto);

        mvc.perform(patch("/api/v1/cards/activate/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карта активирована"));
    }

    @Test
    @DisplayName("DELETE /api/v1/cards/{id} → 200 + пустой ответ")
    void delete_Success() throws Exception {

        mvc.perform(delete("/api/v1/cards/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карта удалена"));
    }

    @Test
    @DisplayName("GET /api/v1/cards/balance/{cardId}/user/{userId} → 200 + баланс")
    void getBalance_Success() throws Exception {
        CardBalanceDTO bal = CardBalanceDTO.builder().balance(BigDecimal.valueOf(123.45)).build();
        given(cardServiceImpl.getBalance(77L, 88L)).willReturn(bal);

        mvc.perform(get("/api/v1/cards/balance/88/user/77"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Баланс карты получен"));
    }

    @Test
    @DisplayName("POST /api/v1/cards/replenish/{id} → 200 + пополнение")
    void replenish_Success() throws Exception {
        ReplenishRequest req = ReplenishRequest.builder().amount(BigDecimal.TEN).build();
        CardBalanceDTO bal = CardBalanceDTO.builder().balance(BigDecimal.valueOf(10)).build();
        given(cardServiceImpl.deposit(90L, BigDecimal.TEN)).willReturn(bal);

        mvc.perform(post("/api/v1/cards/replenish/90")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Баланс успешно пополнен"));
    }
}
