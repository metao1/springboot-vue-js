package com.getyourguide.demo.domain.filter;

import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.domain.Activity;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ActivityFilter {
    private final Map<String, Filter<Activity>> criteria = new HashMap<>();

    public void addCriterion(String key, Filter<Activity> value) {
        criteria.put(key, value);
    }
}
