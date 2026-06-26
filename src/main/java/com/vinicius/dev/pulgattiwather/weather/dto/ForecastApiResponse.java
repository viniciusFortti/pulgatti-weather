package com.vinicius.dev.pulgattiwather.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastApiResponse(
        BigDecimal latitude,
        BigDecimal longitude,
        @JsonProperty("current_weather") CurrentWeather currentWeather,
        HourlyData hourly,
        DailyData daily
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentWeather(
            BigDecimal temperature,
            BigDecimal windspeed,
            BigDecimal winddirection,
            Integer weathercode,
            String time
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record HourlyData(
            List<String> time,
            @JsonProperty("temperature_2m") List<BigDecimal> temperature2m,
            @JsonProperty("relativehumidity_2m") List<Integer> relativehumidity2m,
            List<BigDecimal> precipitation,
            List<Integer> weathercode,
            @JsonProperty("windspeed_10m") List<BigDecimal> windspeed10m,
            @JsonProperty("apparent_temperature") List<BigDecimal> apparentTemperature
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DailyData(
            List<String> time,
            List<Integer> weathercode,
            @JsonProperty("temperature_2m_max") List<BigDecimal> temperature2mMax,
            @JsonProperty("temperature_2m_min") List<BigDecimal> temperature2mMin,
            @JsonProperty("precipitation_sum") List<BigDecimal> precipitationSum,
            @JsonProperty("windspeed_10m_max") List<BigDecimal> windspeed10mMax,
            @JsonProperty("precipitation_probability_max") List<Integer> precipitationProbabilityMax
    ) {
    }
}
