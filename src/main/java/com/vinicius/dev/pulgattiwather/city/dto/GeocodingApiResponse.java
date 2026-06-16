package com.vinicius.dev.pulgattiwather.city.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodingApiResponse(List<Result> results) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            String name,
            BigDecimal latitude,
            BigDecimal longitude,
            String admin1,
            @JsonProperty("country_code") String countryCode
    ) {
    }
}
