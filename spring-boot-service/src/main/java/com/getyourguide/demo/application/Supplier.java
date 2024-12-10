package com.getyourguide.demo.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.getyourguide.demo.domain.Activity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "supplier")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier {
    @Id
    private Long id;
    private String name;
    private String address;
    private int zip;
    private String city;
    private String country;

    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private List<Activity> activities;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;
        Supplier supplier = (Supplier) object;
        return Objects.equals(id, supplier.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
