package com.getyourguide.demo.application.filter;

import com.getyourguide.demo.domain.Activity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterTest {

    @Test
    void testFilterInitialization() {
        Filter<String> filter = new StringFilter("title", "Spring");
        assertNotNull(filter);
        assertEquals("title", filter.getKey());
        assertEquals("Spring", filter.getValue());
    }

    @Test
    void testFilterNullKey() {
        assertThrows(IllegalArgumentException.class, () -> new StringFilter(null, "value"));
    }

    @Test
    void testFilterNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new StringFilter("key", null));
    }

    @Test
    void testMatchesMethodImplemented() {
        Filter<String> filter = new StringFilter("title", "Spring");
        assertNotNull(filter);
    }

    @Nested
    class StringFilterTest {

        @Test
        void testStringFilterMatches() {
            Filter<String> filter = new StringFilter("title", "Spring");
            assertTrue(filter.matches("Spring"));
        }

        @Test
        void testStringFilterNoMatch() {
            Filter<String> filter = new StringFilter("title", "Spring");
            assertTrue(filter.matches("Spring"));
        }
    }

    @Nested
    class NumericFilterTest {

        @Test
        void testNumericFilterMatches() {
            Filter<Number> filter = new NumericFilter("age", 30d);
            assertTrue(filter.matches(30));
        }

        @Test
        void testNumericFilterNoMatch() {
            Filter<Number> filter = new NumericFilter("age", 30d);
            assertFalse(filter.matches(31));
        }
    }

    @Nested
    class DateFilterTest {

        @Test
        void testDateFilterMatches() {
            var ld = LocalDateTime.of(2025, 1, 1, 0, 0, 0, 0);
            Filter<LocalDateTime> filter = new DateFilter("createdDate", ld);
            assertTrue(filter.matches(ld));
        }

        @Test
        void testDateFilterNoMatch() {
            var ld = LocalDateTime.of(2025, 1, 1, 0, 0, 0, 0);
            Filter<LocalDateTime> filter = new DateFilter("createdDate", ld);
            assertFalse(filter.matches(ld.plusDays(1)));
        }
    }

    @Nested
    class FilterCombinationTest {

        @ParameterizedTest
        @MethodSource("provideActivitiesAndExpectedResults")
        @DisplayName("Should filter activities by title and rating")
        void testChainedFilters(List<Activity> activities, int expectedSize, String description) {
            // Activity Rating filter rating equals to 4.0 and above
            Filter<Activity> rateFilter = new RateAboveAndEqualFilter(Activity.builder().rating(4d).build());
            // Activity Title filter contains spring case insensitive, chained filters first
            // by title then by rating
            Filter<Activity> titleFilter = new TitleFilter(Activity.builder().title("spring").build())
                    .andThen(rateFilter);

            var filteredActivities = activities.stream()
                    .filter(titleFilter::matches)
                    .toList();

            assertThat(filteredActivities).as(description).hasSize(expectedSize);
        }

        private static Stream<Arguments> provideActivitiesAndExpectedResults() {
            return Stream.of(
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Boot").build(),
                                    Activity.builder().title("Spring").rating(4d).build(),
                                    Activity.builder().title("Spring Boot").rating(2d).build()),
                            2,
                            "should filter 2 activities"),
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Spring").rating(2d).build(),
                                    Activity.builder().title("Spring").rating(3d).build(),
                                    Activity.builder().title("Spring Boot").rating(2d).build()),
                            3,
                            "Filter by title and rating, all have rating above and equal to 2.0 and title contains spring case insensitive"),
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Spring").rating(1d).build(),
                                    Activity.builder().title("spring boot").rating(1d).build()),
                            2,
                            "Filter by title and rating, all have rating above 1.0 and title contains spring case insensitive"),
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Spring").rating(1d).build(),
                                    Activity.builder().title("spring ").rating(1d).build()),
                            2,
                            "Filter by title and rating, all have rating 1.0"),
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Spring").rating(1d).build(),
                                    Activity.builder().title("Spring").rating(0d).build()),
                            2,
                            "Filter by title and rating, only one has rating below 1.0"),
                    Arguments.of(
                            List.of(
                                    Activity.builder().title("Spring").rating(1d).build(),
                                    Activity.builder().title("Spring").build()),
                            2,
                            "Filter by title and rating, only one has rating 1.0"),
                    Arguments.of(List.of(), 0, "Empty list"),
                    Arguments.of(List.of(Activity.builder().title("Spring").rating(3d).build()),
                            1,
                            "Only one element"));
        }
    }

    private static class RateAboveAndEqualFilter extends Filter<Activity> {

        public RateAboveAndEqualFilter(Activity value) {
            super("rating", value);
        }

        @Override
        public boolean matches(Activity entity) {
            return entity.getRating() >= super.getValue().getRating();
        }
    }

    private static class StringFilter extends Filter<String> {

        public StringFilter(String key, String value) {
            super(key, value);
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key and value must not be null");
            }
        }

        @Override
        public boolean matches(String entity) {
            return entity.contains(super.getValue());
        }
    }

    private static class NumericFilter extends Filter<Number> {

        public NumericFilter(String number, Double maxValue) {
            super(number, maxValue);
        }

        @Override
        public boolean matches(Number entity) {
            return entity.doubleValue() <= super.getValue().doubleValue();
        }
    }

    private static class DateFilter extends Filter<LocalDateTime> {

        public DateFilter(String key, LocalDateTime value) {
            super(key, value);
        }

        @Override
        public boolean matches(LocalDateTime entity) {
            return entity.equals(super.getValue());
        }
    }

}
