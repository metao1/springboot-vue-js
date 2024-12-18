package com.getyourguide.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.getyourguide.demo.application.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@Entity(name = "activity")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    @Id
    private Long id;
    private String title;
    private int price;
    private String currency;
    private double rating;
    private boolean specialOffer;

    @JsonProperty("supplierId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Activity activity = (Activity) object;
        return getId() != null && Objects.equals(getId(), activity.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}
