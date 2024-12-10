package com.getyourguide.demo.presentation;

import com.getyourguide.demo.domain.Activity;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.function.Function;

public class TestUtils {

    public static <T> Matcher<Iterable<?>> matchActivityField(
            List<Activity> pes, Function<Activity, T> extractor
    ) {
        return Matchers.containsInAnyOrder(pes.stream().map(extractor).toArray());
    }
}
