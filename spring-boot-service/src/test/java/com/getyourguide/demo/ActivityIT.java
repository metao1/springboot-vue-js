package com.getyourguide.demo;

import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.service.ActivityService;
import lombok.SneakyThrows;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ActivityService activityService;

    @Test
    @SneakyThrows
    void testActivityResponseContentIncludesExpectedFields() {
        // GIVEN
        final List<Activity> expectedActivities = activityService.getActivities("NONE");

        // WHEN
        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedActivities.size()))
                .andExpect(jsonPath("$[*].id", extractFieldFromActivity(expectedActivities, activity -> activity.getId().intValue())))
                .andExpect(jsonPath("$[*].title", extractFieldFromActivity(expectedActivities, Activity::getTitle)))
                .andExpect(jsonPath("$[*].price", extractFieldFromActivity(expectedActivities, Activity::getPrice)))
                .andExpect(jsonPath("$[*].currency", extractFieldFromActivity(expectedActivities, Activity::getCurrency)))
                .andExpect(jsonPath("$[*].rating", extractFieldFromActivity(expectedActivities, Activity::getRating)))
                .andExpect(jsonPath("$[*].specialOffer", extractFieldFromActivity(expectedActivities, Activity::isSpecialOffer)));
    }

    private <T> Matcher<Iterable<?>> extractFieldFromActivity(
            List<Activity> pes, Function<Activity, T> extractor
    ) {
        return Matchers.containsInAnyOrder(pes.stream().map(extractor).toArray());
    }
}
