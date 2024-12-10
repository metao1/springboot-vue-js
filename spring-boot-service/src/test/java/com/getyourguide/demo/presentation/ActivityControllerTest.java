package com.getyourguide.demo.presentation;

import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.service.ActivityService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.getyourguide.demo.presentation.TestUtils.matchActivityField;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@WebMvcTest(controllers = ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ActivityService activityService;


    public static Stream<Arguments> provideTitleReturnActivities() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Activity(25651L, "Black Forest Cruise", 20, "EUR", 4.5, true, 10L)
                        ),
                        "Black",
                        1
                )
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideTitleReturnActivities")
    @DisplayName("Should return a list of activities according simulated getActivitiesByTitle({0})")
    void testGetActivitiesMethodSimulatingDifferentExpectedResponses(List<Activity> expectedActivities, String title, int expectedSize) {
        // WHEN
        when(activityService.getActivitiesByTitle(title)).thenReturn(expectedActivities);

        // THEN
        mockMvc.perform(get("/activities").queryParam("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedSize))
                .andExpect(jsonPath("$[*].id", matchActivityField(expectedActivities, activity -> activity.getId().intValue())))
                .andExpect(jsonPath("$[*].title", matchActivityField(expectedActivities, Activity::getTitle)))
                .andExpect(jsonPath("$[*].price", matchActivityField(expectedActivities, Activity::getPrice)))
                .andExpect(jsonPath("$[*].currency", matchActivityField(expectedActivities, Activity::getCurrency)))
                .andExpect(jsonPath("$[*].rating", matchActivityField(expectedActivities, Activity::getRating)))
                .andExpect(jsonPath("$[*].specialOffer", matchActivityField(expectedActivities, Activity::isSpecialOffer)))
                .andExpect(jsonPath("$[*].supplierId", matchActivityField(expectedActivities, activity -> activity.getSupplierId().intValue())));
    }
}
