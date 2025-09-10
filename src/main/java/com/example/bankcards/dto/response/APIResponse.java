package com.example.bankcards.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final int status;

    public APIResponse(T data, String message, HttpStatus status) {
        this.success = status.is2xxSuccessful();
        this.message = message;
        this.data = data;
        this.status = status.value();
    }

    public static <T> APIResponse<T> ofSuccess(T data, String message, HttpStatus status) {
        return new APIResponse<>(data, message, status);
    }

    public static <T> APIResponse<T> ofError(String message, HttpStatus status) {
        return new APIResponse<>(null, message, status);
    }
}
