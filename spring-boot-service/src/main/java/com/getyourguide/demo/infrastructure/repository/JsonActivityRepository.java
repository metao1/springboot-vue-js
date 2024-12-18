package com.getyourguide.demo.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.getyourguide.demo.application.Supplier;
import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.infrastructure.deserializer.SupplierDeserializer;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "activity.repository.type", havingValue = "json")
public class JsonActivityRepository implements ActivityRepository {
    private final List<Activity> activities = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final ResourceLoader resourceLoader;

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

            List<Supplier> loadedSuppliers = readFromResources("static/suppliers.json", Supplier.class);
            Map<Long, Supplier> supplierMap = loadedSuppliers.stream()
                    .collect(Collectors.toMap(Supplier::getId, Function.identity()));

            SupplierDeserializer supplierDeserializer = new SupplierDeserializer(supplierMap);
            objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Supplier.class, supplierDeserializer);
            objectMapper.registerModule(module);
            List<Activity> activities = readFromResources("static/activities.json", Activity.class);
            saveAll(activities);
        }
    }

    private <T> List<T> readFromResources(String resourcePath, Class<T> clazz) throws IOException {
        // Read JSON file and convert to a list of activities
        if (resourceLoader == null) {
            throw new IllegalStateException("ResourceLoader is null");
        }
        ClassLoader classLoader = resourceLoader.getClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("ClassLoader is null");
        }
        var fileInputStream = classLoader.getResourceAsStream(resourcePath);
        return objectMapper.readValue(fileInputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
