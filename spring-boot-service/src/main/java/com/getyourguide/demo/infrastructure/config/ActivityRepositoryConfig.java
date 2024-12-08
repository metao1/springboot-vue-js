package com.getyourguide.demo.infrastructure.config;

import com.getyourguide.demo.infrastructure.repository.ActivityRepository;
import com.getyourguide.demo.infrastructure.repository.DatabaseActivityRepository;
import com.getyourguide.demo.infrastructure.repository.JsonActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ActivityRepositoryConfig {

    private final DatabaseActivityRepository databaseActivityRepository;
    private final JsonActivityRepository jsonActivityRepository;

    @Value("${activity.repository.type}")
    private String repositoryType;

    @Bean
    public ActivityRepository activityRepository() {
        return switch (repositoryType.toLowerCase()) {
            case "jpa" -> databaseActivityRepository;
            case "json" -> jsonActivityRepository;
            default -> throw new IllegalArgumentException("Unknown repository type: " + repositoryType);
        };
    }
}
