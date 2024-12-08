package com.getyourguide.demo;

import com.getyourguide.demo.presentation.ActivityController;
import com.getyourguide.demo.domain.service.ActivityService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@WebMvcTest(controllers = ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ActivityService activityExplorer;

    @Test
    @SneakyThrows
    void testGetActivities() {
        mockMvc.perform(get("/activities")).andExpect(status().isOk());
    }
}
