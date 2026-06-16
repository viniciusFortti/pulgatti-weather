package com.vinicius.dev.pulgattiwather.city;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityCoordinatesRepository extends JpaRepository<CityCoordinates, Long> {

    Optional<CityCoordinates> findByCityNameIgnoreCase(String cityName);
}
