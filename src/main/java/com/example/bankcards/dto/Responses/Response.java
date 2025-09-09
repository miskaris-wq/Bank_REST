package com.example.bankcards.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class Response<T> {
    private final T data;
    private final String message;
    private final HttpStatus status;

    public static <T> Response<T> of(T data, String message, HttpStatus status) {
        return new Response<>(data,message, status);
    }

    public static Response<Void> of(String message, HttpStatus status) {
        return new Response<>(null, message, status);
    }
}