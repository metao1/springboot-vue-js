package com.getyourguide.demo.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.getyourguide.demo.domain.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier {
    private Long id;
    private Activity name;
    private Activity address;
    private Activity zip;
    private Activity city;
    private Activity country;
}
