package com.metao.guide.infrastructure.repository;

import com.metao.guide.application.filter.Filter;
import com.metao.guide.application.filter.TitleFilter;
import com.metao.guide.domain.Activity;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "activity.repository.type", havingValue = "jpa")
public class DatabaseActivityRepository implements ActivityRepository {
    private final JpaActivityRepository activityRepository;

    @Override
    public List<Activity> findByFilter(Filter<Activity> filterManager) {
        Specification<Activity> specification = toSpecification(filterManager);
        return activityRepository.findAll(specification);
    }

    @Override
    public void saveAll(List<Activity> activities) {
        activityRepository.saveAll(activities);
    }

    Specification<Activity> toSpecification(Filter<Activity> filter) {
        return (root, query, cb) -> {
            Filter<Activity> f = filter;
            List<Predicate> predicates = new ArrayList<>();
            Predicate predicate;
            while (f != null) {
                resolveFilterValue(f);
                predicate = cb.equal(root.get(f.getKey()), resolveFilterValue(f));
                predicates.add(predicate);
                f = f.getAndThen();
            }
            return predicates.stream().reduce(cb::and).orElse(cb.conjunction());
        };
    }

    private Object resolveFilterValue(Filter<?> filter) {
        return switch (filter.getKey()) {
            case "title" -> ((TitleFilter) filter).getValue().getTitle();
            default -> throw new IllegalArgumentException("Unknown filter key: " + filter.getKey());
        };
    }
}
