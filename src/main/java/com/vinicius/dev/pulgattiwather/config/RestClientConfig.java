package com.vinicius.dev.pulgattiwather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient geocodingRestClient(@Value("${app.open-meteo.geocoding-base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(httpRequestFactory())
                .build();
    }

    @Bean
    public RestClient forecastRestClient(@Value("${app.open-meteo.forecast-base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(httpRequestFactory())
                .build();
    }

    private SimpleClientHttpRequestFactory httpRequestFactory() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15_000);
        factory.setReadTimeout(30_000);
        return factory;
    }
}
