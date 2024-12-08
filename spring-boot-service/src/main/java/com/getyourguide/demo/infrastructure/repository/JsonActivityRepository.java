package com.getyourguide.demo.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.filter.ActivityFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JsonActivityRepository implements ActivityRepository {
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final List<Activity> activities = new ArrayList<>();

    @Override
    public List<Activity> findBySpecification(ActivityFilter specification) {
        return activities.stream()
                .filter(activity -> specification.getCriteria().values().stream()
                        .allMatch((filter) -> filter.matches(activity))
                ).collect(Collectors.toList());
    }

    @EventListener
    @SneakyThrows
    void run(AvailabilityChangeEvent<ReadinessState> event) {
        if (event.getState().equals(ReadinessState.ACCEPTING_TRAFFIC)) {
            List<Activity> loadedActivities = readFromResources("static/activities.json", Activity.class);
            activities.addAll(loadedActivities);
        }
    }

    private <T> List<T> readFromResources(String resourcePath, Class<T> clazz) throws IOException {
        //Read JSON file and convert to a list of activities
        var fileInputStream = resourceLoader.getClassLoader().getResourceAsStream(resourcePath);
        return objectMapper.readValue(fileInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
