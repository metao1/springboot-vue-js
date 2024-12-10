package com.getyourguide.demo.infrastructure.repository;

import com.getyourguide.demo.application.filter.Filter;
import com.getyourguide.demo.domain.Activity;

import java.util.List;


/**
 * This interface defines the contract for interacting with the Activity data.
 */
public interface ActivityRepository {

    /**
     * Retrieves a list of activities based on the provided filter criteria.
     *
     * @param filter The filter criteria to apply when retrieving activities.
     * @return A list of activities that match the given filter criteria.
     */
    List<Activity> findByFilter(Filter<Activity> filter);

    /**
     * Retrieves all activities from the data source.
     *
     * @return A list of all activities.
     */
    List<Activity> findAllActivities();

    /**
     * Saves or updates a list of activities in the data source.
     *
     * @param activities The list of activities to save or update.
     */
    void saveAll(List<Activity> activities);
}
