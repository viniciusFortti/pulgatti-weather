package com.vinicius.dev.pulgattiwather.city;

import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesRequest;
import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesResponse;
import org.springframework.stereotype.Component;

@Component
public class CityCoordinatesMapper {

    public CityCoordinatesResponse toResponse(CityCoordinates city) {
        return new CityCoordinatesResponse(
                city.getId(),
                city.getCityName(),
                city.getState(),
                city.getLatitude(),
                city.getLongitude(),
                city.getUpdatedAt());
    }

    public CityCoordinates toEntity(CityCoordinatesRequest request) {
        return CityCoordinates.builder()
                .cityName(request.cityName())
                .state(request.state())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();
    }
}
