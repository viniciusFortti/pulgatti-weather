package com.vinicius.dev.pulgattiwather.city.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CityCoordinatesRequest(

        @NotBlank(message = "cityName is required")
        @Size(max = 100, message = "cityName must be at most 100 characters")
        String cityName,

        @NotBlank(message = "state is required")
        @Size(max = 2, message = "state must be at most 2 characters")
        String state,

        @NotNull(message = "latitude is required")
        BigDecimal latitude,

        @NotNull(message = "longitude is required")
        BigDecimal longitude
) {
}
