package com.getyourguide.demo.domain.service;

import com.getyourguide.demo.application.filter.TitleFilter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
import com.getyourguide.demo.domain.filter.ActivityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getActivities(String title) {
        ActivityFilter filter = new ActivityFilter();
        final Activity activity = new Activity();
        activity.setTitle(title);
        final var titleFilter = new TitleFilter(activity);
        if (StringUtils.hasText(title)) filter.addCriterion("title", titleFilter);
        return activityRepository.findBySpecification(filter);
    }
}
