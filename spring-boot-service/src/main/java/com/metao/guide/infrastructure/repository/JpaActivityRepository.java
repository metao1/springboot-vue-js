package com.metao.guide.infrastructure.repository;

import com.metao.guide.domain.Activity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "activity.repository.type", havingValue = "jpa")
public interface JpaActivityRepository extends CrudRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
}
