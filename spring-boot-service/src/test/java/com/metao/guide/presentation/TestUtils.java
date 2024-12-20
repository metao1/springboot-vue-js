package com.metao.guide.presentation;

import com.metao.guide.presentation.dto.ActivityDto;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.function.Function;

public class TestUtils {

    public static <T> Matcher<Iterable<?>> matchActivityField(
            List<ActivityDto> pes, Function<ActivityDto, T> extractor
    ) {
        return Matchers.containsInAnyOrder(pes.stream().map(extractor).toArray());
    }
}
