package com.getyourguide.demo.application.filter;

import com.getyourguide.demo.domain.Activity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TitleFilterTest {

    @Test
    void testTitleFilterMatches() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        assertTrue(filter.matches(Activity.builder().title("Build a modern web application").build()));
        assertTrue(filter.matches(Activity.builder().title("Build a modern web application with Spring Boot").build()));
    }

    @Test
    void testTitleFilterDoesNotMatch() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        TitleFilter filter2 = new TitleFilter("title2", Activity.builder().title("Build a modern web application").build());
        assertNotEquals(filter, filter2);
    }

    @Test
    void testTitleFilterDoesNotMatchWithNull() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        assertThrows(NullPointerException.class, () -> filter.matches(null));
    }
    void testTitleFilterHandlesSpecialCharactersInTitle() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("Build a modern-web application!").build());
        assertTrue(filter.matches(Activity.builder().title("Build a modern-web application!").build()));
    }

    @Test
    void testTitleFilterDoesNotMatchWhenActivityIsNull() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        assertThrows(NullPointerException.class, () -> filter.matches(null));
    }

    @Test
    void testTitleFilterEquality() {
        TitleFilter filter1 = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        TitleFilter filter2 = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        assertEquals(filter1, filter2);
    }

    @Test
    void testTitleFilterInequalityWithDifferentTitles() {
        TitleFilter filter1 = new TitleFilter(Activity.builder().title("Build a modern web application").build());
        TitleFilter filter2 = new TitleFilter(Activity.builder().title("Learn advanced data structures").build());
        assertNotEquals(filter1, filter2);
    }

    @Test
    void testTitleFilterMatchesWithWhitespaceDifferences() {
        TitleFilter filter = new TitleFilter(Activity.builder().title(" Build a modern web application ").build());
        assertTrue(filter.matches(Activity.builder().title("Build a modern web application").build()));
    }

    @Test
    void testTitleFilterHandlesNullOrEmptyStringsGracefully() {
        TitleFilter filter = new TitleFilter(Activity.builder().title("").build());
        assertFalse(filter.matches(Activity.builder().title("Build a modern web application").build()));

        filter = new TitleFilter(Activity.builder().title(null).build());
        assertFalse(filter.matches(Activity.builder().title("Build a modern web application").build()));

    }
}