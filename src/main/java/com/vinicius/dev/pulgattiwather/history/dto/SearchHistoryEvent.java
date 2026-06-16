package com.vinicius.dev.pulgattiwather.history.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SearchHistoryEvent(
        Long userId,
        String searchedCity,
        BigDecimal snapshotTemperature,
        Integer snapshotWeatherCode,
        LocalDateTime searchedAt
) {
}
