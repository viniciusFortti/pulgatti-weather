package com.vinicius.dev.pulgattiwather.config;

import com.vinicius.dev.pulgattiwather.weather.dto.ForecastApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {

    @Bean
    public RedisTemplate<String, ForecastApiResponse> forecastRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ForecastApiResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(ForecastApiResponse.class));
        template.afterPropertiesSet();
        return template;
    }
}
