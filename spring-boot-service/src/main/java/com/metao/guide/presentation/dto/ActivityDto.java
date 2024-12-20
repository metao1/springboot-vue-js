package com.metao.guide.presentation.dto;

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
        String supplierCountry,
        String supplierCity,
        String supplierAddress
) {

    @JsonProperty("supplierLocation")
    public String supplierLocation() {
        return supplierCountry + " " + supplierCity + "\n " + supplierAddress;
    }
}
