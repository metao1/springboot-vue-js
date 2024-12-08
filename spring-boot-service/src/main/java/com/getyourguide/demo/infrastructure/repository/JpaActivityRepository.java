package com.getyourguide.demo.infrastructure.repository;

import com.getyourguide.demo.domain.Activity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaActivityRepository extends CrudRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
}
