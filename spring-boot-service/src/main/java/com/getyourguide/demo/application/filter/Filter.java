package com.getyourguide.demo.application.filter;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * This abstract class represents a filter that can be applied to a collection of entities.
 * It provides a mechanism to check if a given entity matches the filter criteria.
 *
 * @param <T> The type of the entities to be filtered.
 */
@Getter
public abstract class Filter<T> {
    private final List<Filter<T>> filters = new ArrayList<>();

    /**
     * The key that identifies the filter criterion.
     */
    private final String key;
    private final T value;

    public Filter(String key, T value) {
        this.key = key;
        this.value = value;
        addFilter(this);
    }

    /**
     * Checks if the given entity matches the filter criteria.
     *
     * @param entity The entity to be checked.
     * @return {@code true} if the entity matches the filter criteria, {@code false} otherwise.
     */
    public abstract boolean matches(T entity);

    public void addFilter(Filter<T> filter) {
        filters.add(filter);
    }

    public boolean allMatches(T t) {
        return filters.stream().allMatch(filter -> filter.matches(t));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter<?> filter = (Filter<?>) o;
        return Objects.equals(key, filter.key);
    }
}
