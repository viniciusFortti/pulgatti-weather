package com.vinicius.dev.pulgattiwather.weather;

import com.vinicius.dev.pulgattiwather.common.exception.ExternalApiException;
import com.vinicius.dev.pulgattiwather.weather.dto.ForecastApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ForecastClient {

    @Qualifier("forecastRestClient")
    private final RestClient forecastRestClient;

    public ForecastApiResponse fetchCurrentWeather(BigDecimal latitude, BigDecimal longitude) {
        try {
            return forecastRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/forecast")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("current_weather", true)
                            .queryParam("hourly", "temperature_2m,relativehumidity_2m,precipitation,weathercode,windspeed_10m,apparent_temperature")
                            .queryParam("daily", "weathercode,temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max,precipitation_probability_max")
                            .queryParam("timezone", "auto")
                            .build())
                    .retrieve()
                    .body(ForecastApiResponse.class);
        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    "Failed to call forecast API for coordinates: " + latitude + ", " + longitude, ex);
        }
    }
}
