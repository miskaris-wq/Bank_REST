package com.example.bankcards.controller;

import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerImplTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthControllerImpl authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_Success() throws Exception {
        JwtDTO dto = JwtDTO.builder().token("t").expirationDate(new Date()).build();
        when(authService.login(any(LoginRequest.class))).thenReturn(dto);

        String json = "{\"userName\":\"u\",\"password\":\"p\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("t"));
    }

    @Test
    void register_Success() throws Exception {
        JwtDTO dto = JwtDTO.builder().token("t").expirationDate(new Date()).build();
        when(authService.register(any(RegisterRequest.class))).thenReturn(dto);

        String json = "{\"userName\":\"u\",\"password\":\"p\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("t"));
    }
}
