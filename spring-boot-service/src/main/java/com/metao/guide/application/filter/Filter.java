package com.metao.guide.application.filter;

import com.metao.guide.domain.Activity;
import lombok.Getter;

import java.util.Objects;


/**
 * This abstract class represents a filter that can be applied to a collection of entities.
 * It provides a mechanism to check if a given entity matches the filter criteria.
 *
 * @param <T> The type of the entities to be filtered.
 */
@Getter
public abstract class Filter<T> {

    public static final Filter<Activity> DEFAULT = new Filter<>("title", Activity.builder().build()) {
        @Override
        public boolean matches(Activity entity) {
            return true;
        }
    };
    /**
     * The key that identifies the filter.
     */
    private final String key;
    private final T value;
    private Filter<T> andThen;

    public Filter<T> andThen(Filter<T> andThen) {
        this.andThen = andThen;
        return this;
    }

    public Filter(String key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Checks if the given entity matches the filter criteria.
     *
     * @param entity The entity to be checked.
     * @return {@code true} if the entity matches the filter criteria, {@code false} otherwise.
     */
    public abstract boolean matches(T entity);

    public boolean allMatches(T t) {
        for (Filter<T> filter = this; filter != null; filter = filter.andThen) {
            if (!filter.matches(t)) {
                return false;
            }
        }
        return true;
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
