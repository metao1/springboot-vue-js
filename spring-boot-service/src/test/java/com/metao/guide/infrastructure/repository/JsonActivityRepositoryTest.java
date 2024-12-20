package com.metao.guide.infrastructure.repository;

import com.metao.guide.application.Supplier;
import com.metao.guide.application.filter.Filter;
import com.metao.guide.application.filter.TitleFilter;
import com.metao.guide.domain.Activity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

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
                        .supplier(Supplier.builder()
                                .id(123L)
                                .address("Black Forest")
                                .city("Berlin")
                                .zip(12345)
                                .country("Germany")
                                .name("Black Forest Cruise")
                                .build())
                        .build(),
                Activity.builder()
                        .id(2L)
                        .title("Activity 2")
                        .price(150)
                        .currency("EUR")
                        .rating(4.0)
                        .specialOffer(false)
                        .supplier(Supplier.builder()
                                .id(456L)
                                .address("Black Forest")
                                .zip(12345)
                                .city("Berlin")
                                .country("Germany")
                                .name("Black Forest Cruise")
                                .build())
                        .build()
        );
        jsonActivityRepository.saveAll(mockActivities);
    }

    @ParameterizedTest
    @MethodSource("provideFiltersAndExpectedResults")
    @SneakyThrows
    void testFindByFilter(Filter<Activity> filter, int expectedSize, List<String> expectedTitles) {
        List<Activity> filteredActivities = jsonActivityRepository.findByFilter(filter);

        assertThat(filteredActivities).hasSize(expectedSize)
                .allSatisfy(activity -> {
                    assertThat(activity.getTitle()).isIn(expectedTitles);
                });
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
                        .supplier(Supplier.builder().id(456L).build())
                        .build()
        );

        jsonActivityRepository.saveAll(newActivities);

        assertThat(jsonActivityRepository.findByFilter(DEFAULT)).hasSize(3);
        assertThat(jsonActivityRepository.findByFilter(DEFAULT).get(2).getTitle()).isEqualTo("Activity 3");
    }

    public static final Filter<Activity> DEFAULT = new Filter<>("title", Activity.builder().build()) {
        @Override
        public boolean matches(Activity entity) {
            return true;
        }
    };

    private static Stream<Arguments> provideFiltersAndExpectedResults() {
        return Stream.of(
                Arguments.of(new TitleFilter(Activity.builder().title("Activity 1").build()), 1, List.of("Activity 1")),
                Arguments.of(new TitleFilter(Activity.builder().title("Activity 2").build()), 1, List.of("Activity 2")),
                Arguments.of(new TitleFilter(Activity.builder().title("Activity ").build()), 2, List.of("Activity 1", "Activity 2"))
        );
    }
}
