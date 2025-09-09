package com.example.bankcards.util;

import org.springframework.data.domain.Page;

import java.util.function.Function;

public final class PageUtils {
    private PageUtils() {}

    public static <E, D> PageResponse<D> map(Page<E> page, Function<E, D> mapper) {
        return PageResponse.<D>builder()
                .items(page.map(mapper).getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .build();
    }
}
