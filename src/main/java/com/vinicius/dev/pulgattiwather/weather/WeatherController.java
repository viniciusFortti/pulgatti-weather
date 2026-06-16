package com.vinicius.dev.pulgattiwather.weather;

import com.vinicius.dev.pulgattiwather.weather.dto.WeatherForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<WeatherForecastResponse> getForecast(@RequestParam String city) {
        return ResponseEntity.ok(weatherService.getCurrentWeather(city));
    }
}
