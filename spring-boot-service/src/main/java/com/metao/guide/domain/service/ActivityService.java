package com.getyourguide.demo.domain.service;

import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.application.filter.PriceFilter;
import com.getyourguide.demo.application.filter.TitleFilter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getActivitiesByFilter(Filter<Activity> filter) {
        return activityRepository.findByFilter(filter).stream().toList();
    }

    private static Filter<Activity> createFilter(String title, int price) {
        // create a list of filters
        final Activity activity = Activity.builder()
                .title(title)
                .price(price)
                .build();
        TitleFilter titleFilter = new TitleFilter(activity);
        PriceFilter priceFilter = new PriceFilter(activity);
        return titleFilter.andThen(priceFilter);
    }
}
