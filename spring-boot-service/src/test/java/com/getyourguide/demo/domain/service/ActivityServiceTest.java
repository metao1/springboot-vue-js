package com.getyourguide.demo.domain.service;

import com.getyourguide.demo.application.filter.TitleFilter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
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
    void getActivitiesByTitle_shouldReturnAllActivitiesWhenTitleIsNone() {
        // Arrange
        String title = "NONE";
        List<Activity> expectedActivities = List.of(
                Activity.builder().title("Activity 1").build(),
                Activity.builder().title("Activity 2").build()
        );
        when(activityRepository.findAllActivities()).thenReturn(expectedActivities);

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByTitle(title);

        // Assert
        assertEquals(expectedActivities, actualActivities);
        verify(activityRepository).findAllActivities();
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getActivitiesByTitle_shouldFilterActivitiesByTitle() {
        // Arrange
        String title = "Hiking";
        Activity filterActivity = Activity.builder().title(title).build();
        TitleFilter titleFilter = new TitleFilter(filterActivity);

        List<Activity> filteredActivities = List.of(
                Activity.builder().title("Hiking in the mountains").build(),
                Activity.builder().title("Hicking trails").build()
        );

        when(activityRepository.findByFilter(titleFilter)).thenReturn(filteredActivities);

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByTitle(title);

        // Assert
        assertEquals(filteredActivities, actualActivities);
        verify(activityRepository).findByFilter(any(TitleFilter.class));
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getActivitiesByTitle_shouldReturnEmptyListWhenTitleIsEmpty() {
        // Arrange
        String title = "";
        when(activityRepository.findAllActivities()).thenReturn(List.of());

        // Act
        List<Activity> actualActivities = activityService.getActivitiesByTitle(title);

        // Assert
        assertEquals(0, actualActivities.size());
        verify(activityRepository).findAllActivities();
        verifyNoMoreInteractions(activityRepository);
    }
}
