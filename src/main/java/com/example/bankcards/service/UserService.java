package com.example.bankcards.service;

import com.example.bankcards.dto.user.UserCreateRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public User getByUsername(String username) {
        return users.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public Long getIdByUsername(String username) {
        return getByUsername(username).getId();
    }

    // ADMIN
    public Page<UserResponse> list(Pageable pageable) {
        return users.findAll(pageable).map(userMapper::toResponse);
    }

    // ADMIN
    public UserResponse get(Long id) {
        return users.findById(id).map(userMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    // ADMIN
    public UserResponse create(UserCreateRequest req) {
        if (users.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        Role role = req.getRole() != null ? Role.valueOf(req.getRole()) : Role.USER;
        String hash = passwordEncoder.encode(req.getPassword());
        User entity = userMapper.fromCreate(req.getUsername(), hash, role);
        return userMapper.toResponse(users.save(entity));
    }

    // ADMIN
    public UserResponse update(Long id, UserUpdateRequest req) {
        User u = users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        if (req.getNewPassword() != null && !req.getNewPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        }
        if (req.getRole() != null && !req.getRole().isBlank()) {
            u.setRole(Role.valueOf(req.getRole()));
        }
        return userMapper.toResponse(users.save(u));
    }

    // ADMIN
    public void delete(Long id) {
        if (!users.existsById(id)) throw new NoSuchElementException("User not found");
        users.deleteById(id);
    }
}
