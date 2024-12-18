package com.getyourguide.demo.presentation;

import com.getyourguide.demo.application.Supplier;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.service.ActivityService;
import com.getyourguide.demo.infrastructure.mapper.ActivityMapper;
import com.getyourguide.demo.presentation.dto.ActivityDto;
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
                                Activity.builder()
                                        .id(25651L)
                                        .title("Black Forest Cruise")
                                        .price(20)
                                        .currency("EUR")
                                        .rating(4.5)
                                        .specialOffer(true)
                                        .supplier(Supplier.builder()
                                                .id(123L)
                                                .name("Cruise Company")
                                                .address("Black Forest Avenue")
                                                .zip(12345)
                                                .city("Berlin")
                                                .country("Germany")
                                                .build())
                                        .build()
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
        when(activityService.getActivitiesByFilter(title)).thenReturn(expectedActivities);
        var expectedActivityDtos = ActivityMapper.mapToDto(expectedActivities);

        // THEN
        mockMvc.perform(get("/activities").queryParam("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedSize))
                .andExpect(jsonPath("$[*].id", matchActivityField(expectedActivityDtos, activity -> activity.id().intValue())))
                .andExpect(jsonPath("$[*].title", matchActivityField(expectedActivityDtos, ActivityDto::title)))
                .andExpect(jsonPath("$[*].price", matchActivityField(expectedActivityDtos, ActivityDto::price)))
                .andExpect(jsonPath("$[*].currency", matchActivityField(expectedActivityDtos, ActivityDto::currency)))
                .andExpect(jsonPath("$[*].rating", matchActivityField(expectedActivityDtos, ActivityDto::rating)))
                .andExpect(jsonPath("$[*].specialOffer", matchActivityField(expectedActivityDtos, ActivityDto::specialOffer)))
                .andExpect(jsonPath("$[*].supplierId", matchActivityField(expectedActivityDtos, activityDto -> activityDto.supplierId().intValue())))
                .andExpect(jsonPath("$[*].supplierName", matchActivityField(expectedActivityDtos, ActivityDto::supplierName)))
                .andExpect(jsonPath("$[*].supplierLocation", matchActivityField(expectedActivityDtos, ActivityDto::supplierLocation)));
    }
}
