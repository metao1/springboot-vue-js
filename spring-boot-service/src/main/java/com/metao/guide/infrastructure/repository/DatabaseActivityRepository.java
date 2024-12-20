package com.metao.guide.infrastructure.repository;

import com.metao.guide.application.filter.Filter;
import com.metao.guide.domain.Activity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@Transactional
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

    Specification<Activity> toSpecification(final Filter<Activity> filter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            for (Filter<Activity> f = filter; f != null && f.getKey() != null && f.getValue() != null; f = f.getAndThen()) {
                predicate = andPredicateFields(cb, predicate, root.get("title"), f.getValue().getTitle(), StringUtils::hasText);
                predicate = andPredicateFields(cb, predicate, root.get("price"), f.getValue().getPrice(), v -> v > 0);
            }
            return predicate;
        };
    }

    private <T> Predicate andPredicateFields(
            CriteriaBuilder criteriaBuilder,
            Predicate predicate,
            Path<?> path,
            T t,
            java.util.function.Predicate<T> predicateFunction
    ) {
        Class<?> pathType = path.getJavaType();
        if (predicateFunction.test(t)) {
            if (pathType == Number.class) {
                return criteriaBuilder.and(predicate, path.in(t));
            }
            return criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), "%" + t.toString().toLowerCase() + "%"));
        }
        return predicate;
    }
}
