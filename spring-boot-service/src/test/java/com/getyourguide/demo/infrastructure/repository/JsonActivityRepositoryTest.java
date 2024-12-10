package com.getyourguide.demo.infrastructure.repository;

import com.getyourguide.demo.application.filter.TitleFilter;
import com.getyourguide.demo.domain.Activity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JsonActivityRepositoryTest {

    @InjectMocks
    private JsonActivityRepository jsonActivityRepository;

    @BeforeEach
    void setUp() {
        List<Activity> mockActivities = List.of(
                Activity.builder()
                        .id(1L)
                        .title("Activity 1")
                        .price(100)
                        .currency("USD")
                        .rating(4.5)
                        .specialOffer(true)
                        .supplierId(123L)
                        .build(),
                Activity.builder()
                        .id(2L)
                        .title("Activity 2")
                        .price(150)
                        .currency("EUR")
                        .rating(4.0)
                        .specialOffer(false)
                        .supplierId(456L)
                        .build()
        );
        jsonActivityRepository.saveAll(mockActivities);
    }

    @Test
    @SneakyThrows
    void testFindByFilter() {
        var titleFilter = new TitleFilter(Activity.builder().title("Activity 1").build());

        List<Activity> filteredActivities = jsonActivityRepository.findByFilter(titleFilter);

        assertThat(filteredActivities).hasSize(1)
                .allSatisfy(activity -> {
                    assertThat(activity.getTitle()).isEqualTo("Activity 1");
                    assertThat(activity.getPrice()).isEqualTo(100);
                    assertThat(activity.getCurrency()).isEqualTo("USD");
                    assertThat(activity.getRating()).isEqualTo(4.5);
                    assertThat(activity.isSpecialOffer()).isTrue();
                    assertThat(activity.getSupplierId()).isEqualTo(123L);
                });

        // add another title filter to the activity filter and check that the result is extended to the second activity
        titleFilter = new TitleFilter(Activity.builder().title("Activity ").build()); // intentionally added a space

        filteredActivities = jsonActivityRepository.findByFilter(titleFilter);

        assertThat(filteredActivities).hasSize(2)
                .allSatisfy(activity -> {
                    assertThat(activity.getTitle()).isIn("Activity 1", "Activity 2");
                    assertThat(activity.getPrice()).isIn(100, 150);
                    assertThat(activity.getCurrency()).isIn("EUR", "USD");
                    assertThat(activity.getRating()).isIn(4.0, 4.5);
                    assertThat(activity.isSpecialOffer()).isIn(false, true);
                    assertThat(activity.getSupplierId()).isIn(456L, 123L);
                });
    }

    @Test
    void testFindAllActivities() {

        List<Activity> allActivities = jsonActivityRepository.findAllActivities();

        assertThat(allActivities).hasSize(2);
        assertThat(allActivities.get(0).getTitle()).isEqualTo("Activity 1");
        assertThat(allActivities.get(1).getTitle()).isEqualTo("Activity 2");
    }

    @Test
    void testSaveAll() {
        List<Activity> newActivities = List.of(
                Activity.builder()
                        .id(3L)
                        .title("Activity 3")
                        .price(200)
                        .currency("GBP")
                        .rating(4.8)
                        .specialOffer(true)
                        .supplierId(789L)
                        .build()
        );

        jsonActivityRepository.saveAll(newActivities);

        assertThat(jsonActivityRepository.findAllActivities()).hasSize(3);
        assertThat(jsonActivityRepository.findAllActivities().get(2).getTitle()).isEqualTo("Activity 3");
    }
}
