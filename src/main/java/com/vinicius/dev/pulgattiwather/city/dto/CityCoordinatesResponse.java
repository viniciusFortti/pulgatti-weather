package com.vinicius.dev.pulgattiwather.city.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CityCoordinatesResponse(
        Long id,
        String cityName,
        String state,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime updatedAt
) {
}
