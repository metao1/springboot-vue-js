package com.getyourguide.demo.infrastructure.config;

import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class ActivityRepositoryConfig {

    private final Map<String, ActivityRepository> activityRepositories;

    @Value("${activity.repository.type}")
    private String repositoryType;

    @Bean
    public ActivityRepository activityRepository() {
        return switch (repositoryType.toLowerCase()) {
            case "jpa" -> activityRepositories.get("jpa");
            case "json" -> activityRepositories.get("json");
            default -> throw new IllegalArgumentException("Unknown repository type: " + repositoryType);
        };
    }
}
