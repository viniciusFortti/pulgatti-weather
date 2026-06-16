package com.vinicius.dev.pulgattiwather.weather.dto;

import java.math.BigDecimal;

public record WeatherForecastResponse(
        String cityName,
        String state,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal temperature,
        BigDecimal windspeed,
        Integer weatherCode,
        String observedAt
) {
}
