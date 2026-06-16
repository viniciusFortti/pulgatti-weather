package com.vinicius.dev.pulgattiwather.weather;

import com.vinicius.dev.pulgattiwather.weather.dto.ForecastApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Cache-aside lookup for the forecast API, backed directly by Redis (not the
 * Spring Cache abstraction) so the TTL and key format stay explicit.
 */
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

        ForecastApiResponse cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        ForecastApiResponse forecast = forecastClient.fetchCurrentWeather(latitude, longitude);
        redisTemplate.opsForValue().set(key, forecast, Duration.ofHours(ttlHours));
        return forecast;
    }
}
