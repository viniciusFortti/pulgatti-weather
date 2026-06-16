package com.vinicius.dev.pulgattiwather.city;

import com.vinicius.dev.pulgattiwather.city.dto.GeocodingApiResponse;
import com.vinicius.dev.pulgattiwather.common.exception.ExternalApiException;
import com.vinicius.dev.pulgattiwather.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeocodingClient {

    @Qualifier("geocodingRestClient")
    private final RestClient geocodingRestClient;

    public GeocodingApiResponse.Result findCity(String cityName) {
        GeocodingApiResponse response;
        try {
            response = geocodingRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/search")
                            .queryParam("name", cityName)
                            .queryParam("count", 1)
                            .queryParam("language", "pt")
                            .build())
                    .retrieve()
                    .body(GeocodingApiResponse.class);
        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to call geocoding API for city: " + cityName, ex);
        }

        return Optional.ofNullable(response)
                .map(GeocodingApiResponse::results)
                .filter(results -> !results.isEmpty())
                .map(results -> results.get(0))
                .orElseThrow(() -> new ResourceNotFoundException("City not found: " + cityName));
    }
}
