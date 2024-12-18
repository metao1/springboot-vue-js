package com.getyourguide.demo.application.filter;

import com.getyourguide.demo.domain.Activity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.springframework.util.StringUtils;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class TitleFilter extends Filter<Activity> {

    public TitleFilter(Activity activity) {
        super("title", activity);
    }

    public TitleFilter(String key, Activity activity) {
        super(key, activity);
    }

    @Override
    public boolean matches(Activity activity) {
        if (!StringUtils.hasText(super.getValue().getTitle()) || !StringUtils.hasText(getValue().getTitle())) {
            return false;
        }
        if (super.getValue().getTitle().equalsIgnoreCase("NONE")) {
            return true;
        }
        return getLowerCase(activity).contains(getLowerCase(super.getValue()));

    }

    private static String getLowerCase(Activity activity) {
        return activity.getTitle().trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}
