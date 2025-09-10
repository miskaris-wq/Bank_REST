package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.service.CardRequestService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardRequestControllerImplTest {

    private MockMvc mvc;

    @Mock
    private CardRequestService cardRequestService;

    @InjectMocks
    private CardRequestControllerImpl cardRequestController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(cardRequestController).build();
    }

    @Test
    @DisplayName("POST /api/v1/card-request/block/{id} → 202 Accepted + созданная заявка")
    void requestBlock_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(55L)
                .cardId(10L)
                .status("PENDING")
                .build();
        given(cardRequestService.requestBlock(10L)).willReturn(dto);

        mvc.perform(post("/api/v1/card-request/block/10"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.requestId").value(55))
                .andExpect(jsonPath("$.data.cardId").value(10))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Заявка на блокировку создана"));
    }

    @Test
    @DisplayName("POST /api/v1/card-request/rejected/{id} → 200 OK + отклонённая заявка")
    void requestRejected_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(66L)
                .cardId(11L)
                .status("REJECTED")
                .build();
        given(cardRequestService.requestRejected(66L)).willReturn(dto);

        mvc.perform(post("/api/v1/card-request/rejected/66"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value(66))
                .andExpect(jsonPath("$.data.cardId").value(11))
                .andExpect(jsonPath("$.data.status").value("REJECTED"))
                .andExpect(jsonPath("$.message").value("Заявка отклонена администратором"));
    }

    @Test
    @DisplayName("GET /api/v1/card-request/{id}/by-user?userId={userId} → 200 OK + заявка пользователя")
    void getIdByUser_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(77L)
                .cardId(20L)
                .status("PENDING")
                .build();
        given(cardRequestService.getIdByUser(77L, 99L)).willReturn(dto);

        mvc.perform(get("/api/v1/card-request/77/by-user").param("userId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value(77))
                .andExpect(jsonPath("$.data.cardId").value(20))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Заявка пользователя получена"));
    }

    @Test
    @DisplayName("GET /api/v1/card-request/{id} → 200 OK + заявка (админ)")
    void getById_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(33L)
                .cardId(5L)
                .status("PENDING")
                .build();
        given(cardRequestService.getId(33L)).willReturn(dto);

        mvc.perform(get("/api/v1/card-request/33"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value(33))
                .andExpect(jsonPath("$.data.cardId").value(5))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Заявка найдена"));
    }

    @Test
    @DisplayName("GET /api/v1/card-request/all/by-user/{userId} → 200 OK + список заявок пользователя")
    void getAllByUser_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(1L)
                .cardId(7L)
                .status("PENDING")
                .build();
        Page<CardRequestDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(1, 3), 1);
        given(cardRequestService.getAllByUser(eq(88L), eq(1), eq(3))).willReturn(page);

        mvc.perform(get("/api/v1/card-request/all/by-user/88").param("page", "1").param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].requestId").value(1))
                .andExpect(jsonPath("$.data.content[0].cardId").value(7))
                .andExpect(jsonPath("$.data.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Заявки пользователя возвращены"));
    }

    @Test
    @DisplayName("GET /api/v1/card-request/all → 200 OK + полный список заявок (админ)")
    void getAll_Success() throws Exception {
        CardRequestDTO dto = CardRequestDTO.builder()
                .requestId(2L)
                .cardId(9L)
                .status("REJECTED")
                .build();
        Page<CardRequestDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1);
        given(cardRequestService.getAll(0, 5)).willReturn(page);

        mvc.perform(get("/api/v1/card-request/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].requestId").value(2))
                .andExpect(jsonPath("$.data.content[0].cardId").value(9))
                .andExpect(jsonPath("$.data.content[0].status").value("REJECTED"))
                .andExpect(jsonPath("$.message").value("Список всех заявок возвращён"));
    }
}
