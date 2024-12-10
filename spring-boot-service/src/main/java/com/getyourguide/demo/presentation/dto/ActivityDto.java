package com.getyourguide.demo.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ActivityDto(
        Long id,
        String title,
        int price,
        String currency,
        double rating,
        boolean specialOffer,
        Long supplierId,
        String supplierName,
        @JsonIgnore
        String supplierCountry,
        @JsonIgnore
        String supplierCity,
        @JsonIgnore
        String supplierAddress
) {

    @JsonProperty("supplierLocation")
    public String supplierLocation() {
        return supplierCountry + " " + supplierCity + "\n " + supplierAddress;
    }
}
