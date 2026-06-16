package com.vinicius.dev.pulgattiwather.history.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SearchHistoryResponse(
        Long id,
        String searchedCity,
        LocalDateTime searchedAt,
        BigDecimal snapshotTemperature,
        Integer snapshotWeatherCode
) {
}
