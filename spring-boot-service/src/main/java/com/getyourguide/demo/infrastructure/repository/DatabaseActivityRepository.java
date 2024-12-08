package com.getyourguide.demo.infrastructure.repository;

import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.filter.ActivityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class DatabaseActivityRepository implements ActivityRepository {
    private final JpaActivityRepository activityRepository;

    @Override
    public List<Activity> findBySpecification(ActivityFilter activityFilter) {
        if (activityFilter == null) {
            return StreamSupport.stream(activityRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
        Specification<Activity> specification = toSpecification(activityFilter);
        return activityRepository.findAll(specification);
    }

    Specification<Activity> toSpecification(ActivityFilter activityFilter) {
        return (root, query, cb) -> activityFilter.getCriteria().values().stream()
                .map(filter -> cb.equal(root.get(filter.getKey()), resolveValueWithReflection(filter)))
                .reduce(cb::and)
                .orElse(cb.conjunction());
    }

    private Object resolveValueWithReflection(Filter<Activity> filter) {
        try {
            var field = Activity.class.getDeclaredField(filter.getKey());
            field.setAccessible(true);
            return field.get(filter.getValue());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Unsupported filter key: " + filter.getKey(), e);
        }
    }
}
