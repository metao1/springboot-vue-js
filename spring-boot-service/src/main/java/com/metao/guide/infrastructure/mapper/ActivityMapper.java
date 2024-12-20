package com.metao.guide.infrastructure.mapper;

import com.metao.guide.domain.Activity;
import com.metao.guide.presentation.dto.ActivityDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ActivityMapper {

    public static List<ActivityDto> mapToDto(List<Activity> activities) {
        return activities.stream()
                .map(activity -> new ActivityDto(activity.getId(),
                        activity.getTitle(),
                        activity.getPrice(),
                        activity.getCurrency(),
                        activity.getRating(),
                        activity.isSpecialOffer(),
                        activity.getSupplier().getId(),
                        activity.getSupplier().getName(),
                        activity.getSupplier().getCountry(),
                        activity.getSupplier().getCity(),
                        activity.getSupplier().getAddress()
                ))
                .toList();
    }

    public static List<Activity> mapToEntity(ActivityDto activityDto) {
        return List.of(Activity.builder()
                .title(activityDto.title())
                .price(activityDto.price())
                .currency(activityDto.currency())
                .rating(activityDto.rating())
                .specialOffer(activityDto.specialOffer())
                .build());
    }
}
