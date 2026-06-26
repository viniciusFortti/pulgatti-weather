package com.vinicius.dev.pulgattiwather.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record WeatherForecastResponse(
        BigDecimal latitude,
        BigDecimal longitude,
        @JsonProperty("current_weather") ForecastApiResponse.CurrentWeather currentWeather,
        ForecastApiResponse.HourlyData hourly,
        ForecastApiResponse.DailyData daily,
        String city
) {
}
