package com.metao.guide.presentation;

import com.metao.guide.application.Supplier;
import com.metao.guide.application.filter.TitleFilter;
import com.metao.guide.domain.Activity;
import com.metao.guide.domain.service.ActivityService;
import com.metao.guide.infrastructure.mapper.ActivityMapper;
import com.metao.guide.presentation.dto.ActivityDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

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
        // GIVEN
        Activity activity = Activity.builder().title(title).build();
        TitleFilter titleFilter = new TitleFilter(activity);

        // WHEN
        when(activityService.getActivitiesByFilter(titleFilter)).thenReturn(expectedActivities);
        var expectedActivityDtos = ActivityMapper.mapToDto(expectedActivities);

        // THEN
        mockMvc.perform(get("/activities").queryParam("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedSize))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", TestUtils.matchActivityField(expectedActivityDtos, activityDto -> activityDto.id().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::title)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].price", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::price)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].currency", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::currency)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].rating", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::rating)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].specialOffer", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::specialOffer)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierId", TestUtils.matchActivityField(expectedActivityDtos, activityDto -> activityDto.supplierId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierName", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::supplierName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].supplierLocation", TestUtils.matchActivityField(expectedActivityDtos, ActivityDto::supplierLocation)));
    }
}
