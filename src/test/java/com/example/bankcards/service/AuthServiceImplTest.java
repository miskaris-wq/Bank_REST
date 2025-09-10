package com.example.bankcards.service;

import com.example.bankcards.dto.payload.JwtDTO;
import com.example.bankcards.dto.requests.LoginRequest;
import com.example.bankcards.dto.requests.RegisterRequest;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.MissingCredentialsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtComponent;
import com.example.bankcards.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtComponent jwtComponent;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPass";
    private static final String TOKEN = "jwt-token";
    private static final Date EXPIRATION = new Date();

    @BeforeEach
    void setUp() {
        lenient().when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        lenient().when(jwtComponent.generateJwtToken(USERNAME)).thenReturn(TOKEN);
        lenient().when(jwtComponent.extractExpiration(TOKEN)).thenReturn(EXPIRATION);
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest(USERNAME, PASSWORD);
        JwtDTO result = authServiceImpl.login(request);
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)
        );
        verify(jwtComponent).generateJwtToken(USERNAME);
        verify(jwtComponent).extractExpiration(TOKEN);
        assertThat(result.getToken()).isEqualTo(TOKEN);
        assertThat(result.getExpirationDate()).isEqualTo(EXPIRATION);
    }

    @Test
    void login_InvalidCredentials() {
        LoginRequest request = new LoginRequest(USERNAME, PASSWORD);
        doThrow(new AuthenticationException("Bad credentials") {}).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertThatThrownBy(() -> authServiceImpl.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Неверные логин или пароль");
        verify(jwtComponent, org.mockito.Mockito.never()).generateJwtToken(any());
    }

    @Test
    void register_MissingCredentials() {
        RegisterRequest request = new RegisterRequest(null, null);
        assertThatThrownBy(() -> authServiceImpl.register(request))
                .isInstanceOf(MissingCredentialsException.class)
                .hasMessage("Не указан логин или пароль");
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest(USERNAME, PASSWORD);
        JwtDTO result = authServiceImpl.register(request);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo(USERNAME);
        assertThat(savedUser.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(savedUser.getBankCartList()).isEqualTo(Collections.emptyList());
        verify(jwtComponent).generateJwtToken(USERNAME);
        verify(jwtComponent).extractExpiration(TOKEN);
        assertThat(result.getToken()).isEqualTo(TOKEN);
        assertThat(result.getExpirationDate()).isEqualTo(EXPIRATION);
    }
}
