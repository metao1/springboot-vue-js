package com.metao.guide.presentation;

import com.metao.guide.domain.service.ActivityService;
import com.metao.guide.infrastructure.mapper.ActivityMapper;
import com.metao.guide.presentation.dto.ActivityDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(properties = "activity.repository.type=json")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ActivityService activityService;

    public static Stream<Arguments> provideTitleAndExpectedActivitiesSize() {
            return Stream.of(
                    Arguments.of("no title provided", null, 14),
                    Arguments.of("empty title", "", 14),
                    Arguments.of("Berlin with capital letters as title provided", "Berlin", 8),
                    Arguments.of("title provided should be case insensitive", "berlin", 8),
                    Arguments.of("title provided should be case insensitive", "berlin ", 8),
                    Arguments.of("title provided should be case insensitive and trimmed", "   berlin  ", 8),
                    Arguments.of("title provided should be case insensitive and trimmed and with leading/trailing spaces", "   BERLI  ", 8),
                    Arguments.of("title provided should be case insensitive and trimmed and with leading/trailing spaces and with special characters", "   BERLI!  ", 8));
    }

    @SneakyThrows
    @ParameterizedTest(name = "Filter by title: case `{0}` for title with value '{1}' should return {2} activities")
    @MethodSource("provideTitleAndExpectedActivitiesSize")
    @DisplayName("Should return a list of activities according to the title filter")
    void testGetActivitiesMethodWithDifferentFilterValues(
            String description,     // Test case description
            String title,          // Input title
            int expectedSize       // Expected result size
    ) {
        // GIVEN
        var filter = ActivityService.createFilter(title, 0);
        var expectedActivities = activityService.getActivitiesByFilter(filter);

        var expectedActivityDtos = ActivityMapper.mapToDto(expectedActivities);

        // THEN
        mockMvc.perform(get("/api/activities").queryParam("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedSize))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id",
                        TestUtils.matchActivityField(expectedActivityDtos, activityDto -> activityDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::title)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].price",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::price)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].currency",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::currency)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].rating",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::rating)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].specialOffer",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::specialOffer)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierId",
                        TestUtils.matchActivityField(expectedActivityDtos,
                                activityDto -> activityDto.supplierId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierName",
                        TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::supplierName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierLocation", TestUtils.matchActivityField(expectedActivityDtos,
                        ActivityDto::supplierLocation)));
    }
}
