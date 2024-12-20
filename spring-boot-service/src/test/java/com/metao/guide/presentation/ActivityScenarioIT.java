package com.metao.guide.presentation;

import com.metao.guide.application.Supplier;
import com.metao.guide.domain.Activity;
import com.metao.guide.domain.service.ActivityService;
import com.metao.guide.infrastructure.mapper.ActivityMapper;
import com.metao.guide.presentation.dto.ActivityDto;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "activity.repository.type=jpa")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActivityScenarioIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ActivityService activityService;

    @ParameterizedTest
    @CsvSource({
            ",20",
            "title, 20",
            "title2, 0",
            "t, 0",
            "tit, -1",
            "title4, 1000000"
    })
    @SneakyThrows
    void testWhenSaveAnActivityThenGetActivityReturns(String title, int price) {
        // GIVEN
        var newActivities = List.of(
                Activity.builder()
                        .id(1L)
                        .title("title")
                        .price(20)
                        .currency("EUR")
                        .rating(4.5)
                        .specialOffer(true)
                        .supplier(Supplier.builder()
                                .name("Cruise Company")
                                .address("Black Forest Avenue")
                                .zip(12345)
                                .city("Berlin")
                                .country("Germany")
                                .build())
                        .build(),
                Activity.builder()
                        .id(2L)
                        .title("title2")
                        .price(20)
                        .currency("EUR")
                        .rating(4.5)
                        .specialOffer(true)
                        .supplier(Supplier.builder()
                                .name("Cruise Company")
                                .address("Black Forest Avenue")
                                .zip(12345)
                                .city("Berlin")
                                .country("Germany")
                                .build())
                        .build()
        );
        var filter = ActivityService.createFilter(title, price);
        activityService.saveAll(newActivities);
        var expectedActivities = activityService.getActivitiesByFilter(filter);
        var expectedActivityDtos = ActivityMapper.mapToDto(expectedActivities);
        String url = "/api/activities?price=" + price;
        if (title != null) {
            url += "&title=" + title;
        }
        // THEN
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedActivityDtos.size()))
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
