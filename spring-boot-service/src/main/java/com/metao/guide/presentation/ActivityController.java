package com.metao.guide.presentation;

import com.metao.guide.domain.Activity;
import com.metao.guide.domain.service.ActivityService;
import com.metao.guide.infrastructure.mapper.ActivityMapper;
import com.metao.guide.presentation.dto.ActivityDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/debug")
    public void debug(@RequestParam(name = "title", required = false) String title,
                      @RequestParam(name = "price", required = false) Integer price,
                      Model model) {
        model.addAttribute("title", title);
        model.addAttribute("price", price);
        var filter = ActivityService.createFilter(title, price);

        var activities = activityService.getActivitiesByFilter(filter);
        final List<ActivityDto> activityDtos = ActivityMapper.mapToDto(activities);
        model.addAttribute("activities", activityDtos);
    }

    /**
     * Retrieve a list of {@link Activity} objects from the database.
     *
     * @param title If not equal to "NONE", only activities with titles
     *              matching this string will be returned.
     *              Otherwise, all activities will be returned.
     * @return A JSON response containing the results of the query.
     */
    @GetMapping
    public ResponseEntity<List<ActivityDto>> activities(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "price", required = false) Integer price
    ) {
        var filter = ActivityService.createFilter(title, price);
        final List<Activity> activities = activityService.getActivitiesByFilter(filter);
        final List<ActivityDto> activityDtos = ActivityMapper.mapToDto(activities);
        return ResponseEntity.ok(activityDtos);
    }

    @PostMapping
    public ResponseEntity<Void> saveActivities(@Validated @RequestBody ActivityDto activityDto) {
        final List<Activity> activities = ActivityMapper.mapToEntity(activityDto);
        activityService.saveAll(activities);
        return ResponseEntity.ok().build();
    }
}
