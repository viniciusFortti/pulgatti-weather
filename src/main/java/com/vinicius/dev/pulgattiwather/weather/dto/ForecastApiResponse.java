package com.vinicius.dev.pulgattiwather.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastApiResponse(
        BigDecimal latitude,
        BigDecimal longitude,
        @JsonProperty("current_weather") CurrentWeather currentWeather
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentWeather(
            BigDecimal temperature,
            BigDecimal windspeed,
            Integer weathercode,
            String time
    ) {
    }
}
