package com.getyourguide.demo.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.domain.Activity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "activity.repository.type", havingValue = "json")
public class JsonActivityRepository implements ActivityRepository {
    private final List<Activity> activities = new ArrayList<>();
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Override
    public List<Activity> findByFilter(Filter<Activity> filter) {
        return activities.stream().filter(filter::allMatches).toList();
    }

    @Override
    public List<Activity> findAllActivities() {
        return activities;
    }

    @Override
    public void saveAll(List<Activity> activities) {
        this.activities.addAll(activities);
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
        if (resourceLoader == null || resourceLoader.getClassLoader() == null) {
            throw new IllegalStateException("ResourceLoader is null");
        }
        var fileInputStream = resourceLoader.getClassLoader().getResourceAsStream(resourcePath);
        return objectMapper.readValue(fileInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
