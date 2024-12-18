package com.metao.guide.domain.service;

import com.metao.guide.application.filter.Filter;
import com.metao.guide.application.filter.TitleFilter;
import com.metao.guide.domain.Activity;
import com.metao.guide.infrastructure.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository);
    }

    @Test
    void getActivitiesByTitle_shouldReturnAllActivitiesWhenFilterIsNone() {
        // Arrange
        String title = "NONE";
        Activity activity = Activity.builder().title(title).build();
        TitleFilter titleFilter = new TitleFilter(activity);

        List<Activity> expectedActivities = List.of(
                Activity.builder().title("Activity 1").build(),
                Activity.builder().title("Activity 2").build()
        );

        when(activityRepository.findByFilter(titleFilter)).thenReturn(expectedActivities);

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByFilter(titleFilter);

        // Assert
        assertEquals(expectedActivities, actualActivities);
        verify(activityRepository).findByFilter(titleFilter);
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getActivitiesByTitle_shouldFilterActivitiesByFilter() {
        // Arrange
        String title = "Hiking";
        Activity activity = Activity.builder().title(title).build();
        TitleFilter titleFilter = new TitleFilter(activity);

        List<Activity> filteredActivities = List.of(
                Activity.builder().title("Hiking in the mountains").build(),
                Activity.builder().title("Hicking trails").build()
        );

        when(activityRepository.findByFilter(titleFilter)).thenReturn(filteredActivities);

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByFilter(titleFilter);

        // Assert
        assertEquals(filteredActivities, actualActivities);
        verify(activityRepository).findByFilter(any(TitleFilter.class));
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getActivitiesByTitle_shouldReturnEmptyListWhenFilterIsEmpty() {
        // Arrange
        String title = "";
        Activity activity = Activity.builder().title(title).build();
        TitleFilter titleFilter = new TitleFilter(activity);
        when(activityRepository.findByFilter(any(TitleFilter.class))).thenReturn(List.of());

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByFilter(titleFilter);

        // Assert
        assertEquals(0, actualActivities.size());
        verify(activityRepository).findByFilter(titleFilter);
        verifyNoMoreInteractions(activityRepository);
    }
}
