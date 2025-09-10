package com.example.bankcards.controller;

import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.service.TransferService;
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
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransferControllerImplTest {

    private MockMvc mvc;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferControllerImpl transferController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    private static String toJson(Object obj) throws Exception {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }

    @Test
    @DisplayName("POST /api/v1/transfer/user/{userId} → успешный перевод")
    void transferFromToCardUser_Success() throws Exception {
        TransferUserRequest req = TransferUserRequest.builder()
                .fromCardId(1L)
                .toCardId(2L)
                .amount(BigDecimal.valueOf(100))
                .build();

        TransferUserDto dto = TransferUserDto.builder()
                .transactionId(123L)
                .initiatorId(7L)
                .amount(BigDecimal.valueOf(100))
                .createdAt(Instant.now())
                .build();

        given(transferService.transferFromToCardUser(eq(7L), any(TransferUserRequest.class))).willReturn(dto);

        mvc.perform(post("/api/v1/transfer/user/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactionId").value(123))
                .andExpect(jsonPath("$.data.initiatorId").value(7))
                .andExpect(jsonPath("$.data.amount").value(100))
                .andExpect(jsonPath("$.message").value("Перевод успешно выполнен"));
    }

    @Test
    @DisplayName("GET /api/v1/transfer/all → список всех переводов")
    void getAll_Success() throws Exception {
        TransferUserDto dto = TransferUserDto.builder()
                .transactionId(201L)
                .initiatorId(1L)
                .amount(BigDecimal.valueOf(50))
                .createdAt(Instant.now())
                .build();

        Page<TransferUserDto> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1);
        given(transferService.getAll(0, 5)).willReturn(page);

        mvc.perform(get("/api/v1/transfer/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].transactionId").value(201))
                .andExpect(jsonPath("$.data.content[0].initiatorId").value(1))
                .andExpect(jsonPath("$.message").value("Все переводы возвращены"));
    }

    @Test
    @DisplayName("GET /api/v1/transfer/by-user/{userId} → переводы конкретного пользователя")
    void getAllByUser_Success() throws Exception {
        TransferUserDto dto = TransferUserDto.builder()
                .transactionId(301L)
                .initiatorId(88L)
                .amount(BigDecimal.valueOf(75))
                .createdAt(Instant.now())
                .build();

        Page<TransferUserDto> page = new PageImpl<>(List.of(dto), PageRequest.of(1, 3), 1);
        given(transferService.getAllByUser(1, 3, 88L)).willReturn(page);

        mvc.perform(get("/api/v1/transfer/by-user/88")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].transactionId").value(301))
                .andExpect(jsonPath("$.data.content[0].initiatorId").value(88))
                .andExpect(jsonPath("$.message").value("История переводов пользователя получена"));
    }

    @Test
    @DisplayName("GET /api/v1/transfer/{id} → один перевод")
    void getById_Success() throws Exception {
        TransferUserDto dto = TransferUserDto.builder()
                .transactionId(401L)
                .initiatorId(2L)
                .amount(BigDecimal.valueOf(200))
                .createdAt(Instant.now())
                .build();

        given(transferService.getById(401L)).willReturn(dto);

        mvc.perform(get("/api/v1/transfer/401"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactionId").value(401))
                .andExpect(jsonPath("$.data.initiatorId").value(2))
                .andExpect(jsonPath("$.message").value("Перевод найден"));
    }
}
