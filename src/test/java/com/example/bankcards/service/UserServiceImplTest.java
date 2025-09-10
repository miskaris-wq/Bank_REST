package com.example.bankcards.service;

import com.example.bankcards.dto.payload.CardBalanceDTO;
import com.example.bankcards.dto.payload.UserDTO;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CustomUserNotFoundException;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CardServiceImpl cardServiceImpl;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserDTO userDto;
    private CardBalanceDTO balanceDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setRoles(Set.of(Role.ADMIN));
        userDto = new UserDTO();
        balanceDto = new CardBalanceDTO();
        balanceDto.setBalance(java.math.BigDecimal.ZERO);
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setRoles(Set.of(Role.ADMIN));
        userDto = new UserDTO();
        balanceDto = new CardBalanceDTO();
    }

    @Test
    void getUserByUsername_Success() {
        when(userRepository.findByUsername("u")).thenReturn(Optional.of(user));
        User result = userServiceImpl.getUserByUsername("u");
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userRepository.findByUsername("u")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userServiceImpl.getUserByUsername("u"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getCurrentUser_Success() {
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        SecurityContext context = org.mockito.Mockito.mock(SecurityContext.class);
        when(auth.getName()).thenReturn("u");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
        when(userRepository.findByUsername("u")).thenReturn(Optional.of(user));
        User result = userServiceImpl.getCurrentUser();
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByUsername_Page_Success() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(PageRequest.of(0,5))).thenReturn(page);
        when(userMapper.toDto(user)).thenReturn(userDto);
        Page<UserDTO> result = userServiceImpl.getUserByUsername(0,5);
        assertThat(result.getContent()).containsExactly(userDto);
    }

    @Test
    void getUserByUsername_Page_Empty() {
        when(userRepository.findAll(PageRequest.of(0,5))).thenReturn(Page.empty());
        assertThatThrownBy(() -> userServiceImpl.getUserByUsername(0,5))
                .isInstanceOf(CustomUserNotFoundException.class);
    }

    @Test
    void delete_Success() {
        userServiceImpl.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDTO result = userServiceImpl.getUserById(1L);
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userServiceImpl.getUserById(1L))
                .isInstanceOf(CustomUserNotFoundException.class);
    }

    @Test
    void update_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userServiceImpl.update(1L, new UserDTO()))
                .isInstanceOf(CustomUserNotFoundException.class);
    }
}
