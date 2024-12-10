package com.getyourguide.demo.domain.service;

import com.getyourguide.demo.application.filter.TitleFilter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getActivitiesByTitle(String title) {
        if (!StringUtils.hasText(title) || title.equals("NONE")) {
            return activityRepository.findAllActivities();
        }
        // create a list of filters
        final Activity activity = Activity.builder()
                .title(title)
                .build();
        final var titleFilter = new TitleFilter(activity);

        return activityRepository.findByFilter(titleFilter).stream().toList();
    }
}
