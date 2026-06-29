package com.vinicius.dev.pulgattiwather.weather;

import com.vinicius.dev.pulgattiwather.weather.dto.ForecastApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForecastCacheService {

    private static final String CACHE_KEY_PREFIX = "weather-forecast:";

    private final ForecastClient forecastClient;
    private final RedisTemplate<String, ForecastApiResponse> redisTemplate;

    @Value("${app.cache.weather-forecast-ttl-hours}")
    private long ttlHours;

    public ForecastApiResponse getCurrentWeather(BigDecimal latitude, BigDecimal longitude) {
        String key = CACHE_KEY_PREFIX + latitude + ":" + longitude;

        ForecastApiResponse cached = null;
        try {
            cached = redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            log.warn("Redis cache read failed for key={}: [{}] {}", key, ex.getClass().getSimpleName(), ex.getMessage());
        }
        if (cached != null && cached.hourly() != null) {
            log.debug("Cache HIT for key={}", key);
            return cached;
        }
        log.debug("Cache MISS for key={}", key);

        ForecastApiResponse forecast = forecastClient.fetchCurrentWeather(latitude, longitude);

        try {
            redisTemplate.opsForValue().set(key, forecast, Duration.ofHours(ttlHours));
            log.debug("Cache WRITE OK for key={}", key);
        } catch (Exception ex) {
            log.error("Redis cache write failed for key={}: [{}] {}", key, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        return forecast;
    }
}
