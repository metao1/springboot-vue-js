package com.getyourguide.demo.presentation;

import com.getyourguide.demo.domain.Activity;
import com.getyourguide.demo.domain.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/debug")
    public void debug(@RequestParam(name = "title", required = false, defaultValue = "NONE") String title, Model model) {
        model.addAttribute("title", title);
        var activities = activityService.getActivitiesByTitle(title);
        model.addAttribute("activities", activities);
    }

    /**
     * Retrieve a list of {@link Activity} objects from the database.
     *
     * @param title If not equal to "NONE", only activities with titles
     *              matching this string will be returned.
     *              Otherwise, all activities will be returned.
     * @return A JSON response containing the results of the query.
     */
    @GetMapping("/activities")
    public ResponseEntity<List<Activity>> activities(
            @RequestParam(name = "title", required = false, defaultValue = "NONE") String title) {
        final List<Activity> activities = activityService.getActivitiesByTitle(title);
        return ResponseEntity.ok(activities);
    }
}
