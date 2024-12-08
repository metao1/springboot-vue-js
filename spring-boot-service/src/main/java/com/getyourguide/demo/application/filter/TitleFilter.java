package com.getyourguide.demo.application.filter;

import com.getyourguide.demo.domain.Activity;
import lombok.Getter;

@Getter
public class TitleFilter extends Filter<Activity> {

    public TitleFilter(Activity value) {
        super("title", value);
    }

    @Override
    public boolean matches(Activity activity) {
        return activity.getTitle().equalsIgnoreCase(super.getValue().getTitle());
    }

}
