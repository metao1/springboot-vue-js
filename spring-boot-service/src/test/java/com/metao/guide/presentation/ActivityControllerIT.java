package com.getyourguide.demo.presentation;

import com.getyourguide.demo.domain.service.ActivityService;
import com.getyourguide.demo.infrastructure.mapper.ActivityMapper;
import com.getyourguide.demo.presentation.dto.ActivityDto;
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

import java.util.stream.Stream;

import static com.getyourguide.demo.presentation.TestUtils.matchActivityField;
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
                                Arguments.of(
                                                "no title provided",
                                                null, 14),
                                Arguments.of(
                                                "NONE as title provided",
                                                "NONE", 14),
                                Arguments.of(
                                                "Berlin with capital letters as title provided",
                                                "Berlin", 8),
                                Arguments.of(
                                                "title provided should be case insensitive",
                                                "berlin", 8),
                                Arguments.of(
                                                "title provided should be case insensitive",
                                                "berlin ", 8),
                                Arguments.of(
                                                "title provided should be case insensitive and trimmed",
                                                "   berlin  ", 8),
                                Arguments.of(
                                                "title provided should be case insensitive and trimmed and with leading/trailing spaces",
                                                "   BERLI  ", 8),
                                Arguments.of(
                                                "title provided should be case insensitive and trimmed and with leading/trailing spaces and with special characters",
                                                "   BERLI!  ", 8));
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("provideTitleAndExpectedActivitiesSize")
        @DisplayName("Should return a list of activities according to the title filter")
        void testGetActivitiesMethodWithDifferentFilterValues(String description, String title, int expectedSize) {
                // GIVEN
                var expectedActivities = activityService.getActivitiesByFilter(title);

                var expectedActivityDtos = ActivityMapper.mapToDto(expectedActivities);

                // THEN
                mockMvc.perform(get("/activities").queryParam("title", title))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()").value(expectedSize))
                                .andExpect(jsonPath("$[*].id",
                                                matchActivityField(expectedActivityDtos,
                                                                activity -> activity.id().intValue())))
                                .andExpect(jsonPath("$[*].title",
                                                matchActivityField(expectedActivityDtos, ActivityDto::title)))
                                .andExpect(jsonPath("$[*].price",
                                                matchActivityField(expectedActivityDtos, ActivityDto::price)))
                                .andExpect(jsonPath("$[*].currency",
                                                matchActivityField(expectedActivityDtos, ActivityDto::currency)))
                                .andExpect(jsonPath("$[*].rating",
                                                matchActivityField(expectedActivityDtos, ActivityDto::rating)))
                                .andExpect(jsonPath("$[*].specialOffer",
                                                matchActivityField(expectedActivityDtos, ActivityDto::specialOffer)))
                                .andExpect(jsonPath("$[*].supplierId",
                                                matchActivityField(expectedActivityDtos,
                                                                activityDto -> activityDto.supplierId().intValue())))
                                .andExpect(jsonPath("$[*].supplierName",
                                                matchActivityField(expectedActivityDtos, ActivityDto::supplierName)))
                                .andExpect(jsonPath("$[*].supplierLocation", matchActivityField(expectedActivityDtos,
                                                ActivityDto::supplierLocation)));
        }

}
