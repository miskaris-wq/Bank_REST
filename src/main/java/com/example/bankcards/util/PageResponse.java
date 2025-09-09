package com.example.bankcards.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    List<T> items;
    int page;
    int size;
    long total;
}
