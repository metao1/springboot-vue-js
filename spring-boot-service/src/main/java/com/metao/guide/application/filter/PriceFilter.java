package com.metao.guide.application.filter;

import com.metao.guide.domain.Activity;

public class PriceFilter extends Filter<Activity> {

    public PriceFilter(Activity value) {
        super("price", value);
    }

    /**
     * Checks if the given entity matches the filter criteria.
     *
     * @param entity The entity to be checked.
     * @return {@code true} if the entity matches the filter criteria, {@code false} otherwise.
     */
    @Override
    public boolean matches(Activity entity) {
        if (super.getValue().getPrice() == 0) {
            return true;
        }
        return entity.getPrice() <= super.getValue().getPrice();
    }
}
