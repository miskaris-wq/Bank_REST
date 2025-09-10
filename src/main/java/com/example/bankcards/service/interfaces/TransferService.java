package com.example.bankcards.service.interfaces;

import com.example.bankcards.dto.requests.TransferUserRequest;
import com.example.bankcards.dto.payload.TransferUserDto;
import org.springframework.data.domain.Page;

public interface TransferService {
    TransferUserDto transferFromToCardUser(Long userId, TransferUserRequest request);

    Page<TransferUserDto> getAll(int pageNumber, int pageSize);

    TransferUserDto getById(Long id);

    Page<TransferUserDto> getAllByUser(int pageNumber, int pageSize, Long userId);
}
