package com.example.bankcards.service.impl;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CustomUserNotFoundException;
import com.example.bankcards.mappers.BankCardBalanceMapper;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BankCardBalanceMapper bankCardBalanceMapper;
    private final PasswordEncoder passwordEncoder;
    private final CardServiceImpl cardServiceImpl;

    @Override
    @Cacheable(cacheNames = "user", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomUserNotFoundException(
                        "Пользователь с именем " + username + " не найден"
                ));
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#pageNumber + '-' + #pageSize")
    public Page<UserDTO> getUserByUsername(int pageNumber, int pageSize) {
        Page<User> users = userRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if (users.isEmpty()) {
            throw new CustomUserNotFoundException("Список пользователей пуст");
        }

        return users.map(userMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"user", "users", "currentUser", "login", "register"}, allEntries = true)
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserNotFoundException(
                        "Пользователь с id=" + id + " не найден"
                ));

        return userMapper.toDto(user);
    }

    @Override
    @Cacheable(cacheNames = "balanceUser", key = "#userId")
    public TotalCardBalanceDTO getTotalBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomUserNotFoundException(
                        "Пользователь с id=" + userId + " не найден"
                ));

        List<BankCardDTO> cards = cardServiceImpl.getAllCurrentUser();
        List<CardBalanceDto> balances = cards.stream()
                .map(bankCardBalanceMapper::toDto)
                .toList();

        if (balances.isEmpty()) {
            throw new CardNotFoundException("Нет карт для расчёта баланса у пользователя id=" + userId);
        }

        BigDecimal total = balances.stream()
                .map(CardBalanceDto::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TotalCardBalanceDTO.builder()
                .userId(user.getId())
                .cardBalances(balances)
                .totalBalance(total)
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"user", "users", "currentUser", "login", "register"}, allEntries = true)
    public UserDTO update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserNotFoundException(
                        "Пользователь с id=" + id + " не найден"
                ));

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }
}
