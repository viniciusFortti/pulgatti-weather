package com.vinicius.dev.pulgattiwather.weather;

import com.vinicius.dev.pulgattiwather.city.CityCoordinates;
import com.vinicius.dev.pulgattiwather.city.CityCoordinatesService;
import com.vinicius.dev.pulgattiwather.common.exception.ResourceNotFoundException;
import com.vinicius.dev.pulgattiwather.history.SearchHistoryProducer;
import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryEvent;
import com.vinicius.dev.pulgattiwather.user.User;
import com.vinicius.dev.pulgattiwather.user.UserRepository;
import com.vinicius.dev.pulgattiwather.weather.dto.ForecastApiResponse;
import com.vinicius.dev.pulgattiwather.weather.dto.WeatherForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final CityCoordinatesService cityCoordinatesService;
    private final ForecastCacheService forecastCacheService;
    private final UserRepository userRepository;
    private final SearchHistoryProducer searchHistoryProducer;

    public WeatherForecastResponse getCurrentWeather(String cityName) {
        CityCoordinates city = cityCoordinatesService.getOrFetch(cityName);

        ForecastApiResponse forecast = forecastCacheService.getCurrentWeather(city.getLatitude(), city.getLongitude());
        ForecastApiResponse.CurrentWeather currentWeather = forecast.currentWeather();

        publishSearchHistoryEvent(city, currentWeather);

        return new WeatherForecastResponse(
                forecast.latitude(),
                forecast.longitude(),
                currentWeather,
                forecast.hourly(),
                forecast.daily(),
                city.getCityName());
    }

    private void publishSearchHistoryEvent(CityCoordinates city, ForecastApiResponse.CurrentWeather currentWeather) {
        Long userId = currentUser().getId();

        SearchHistoryEvent event = new SearchHistoryEvent(
                userId,
                city.getCityName(),
                currentWeather.temperature(),
                currentWeather.weathercode(),
                LocalDateTime.now());

        searchHistoryProducer.publish(event);
    }

    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email));
    }
}
