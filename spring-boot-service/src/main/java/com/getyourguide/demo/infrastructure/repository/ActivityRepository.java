package com.getyourguide.demo.infrastructure.repository;

import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.filter.ActivityFilter;

import java.util.List;

public interface ActivityRepository {
    List<Activity> findBySpecification(ActivityFilter filter);
}
