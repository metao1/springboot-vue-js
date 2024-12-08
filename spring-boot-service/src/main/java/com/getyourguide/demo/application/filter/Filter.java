package com.getyourguide.demo.application.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Filter<T> {
    private final String key;
    private final T value;

    public abstract boolean matches(T entity);
}
