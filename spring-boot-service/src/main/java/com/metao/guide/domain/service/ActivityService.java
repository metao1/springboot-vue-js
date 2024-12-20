package com.metao.guide.domain.service;

import com.metao.guide.application.filter.Filter;
import com.metao.guide.application.filter.PriceFilter;
import com.metao.guide.application.filter.TitleFilter;
import com.metao.guide.domain.Activity;
import com.metao.guide.infrastructure.repository.ActivityRepository;
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

    public static Filter<Activity> createFilter(String title, Integer price) {
        // create a list of filters
        final Activity activity = Activity.builder()
                .title(title)
                .price(price == null ? 0 : price)
                .build();
        TitleFilter titleFilter = new TitleFilter(activity);
        PriceFilter priceFilter = new PriceFilter(activity);
        return titleFilter.andThen(priceFilter);
    }

    public void saveAll(List<Activity> activities) {
        activityRepository.saveAll(activities);
    }
}
