package com.vinicius.dev.pulgattiwather.city;

import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesRequest;
import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesResponse;
import com.vinicius.dev.pulgattiwather.city.dto.GeocodingApiResponse;
import com.vinicius.dev.pulgattiwather.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityCoordinatesService {

    private static final String DEFAULT_STATE = "RS";

    private final CityCoordinatesRepository cityCoordinatesRepository;
    private final CityCoordinatesMapper cityCoordinatesMapper;
    private final GeocodingClient geocodingClient;

    public List<CityCoordinatesResponse> findAll() {
        return cityCoordinatesRepository.findAll().stream()
                .map(cityCoordinatesMapper::toResponse)
                .toList();
    }

    public CityCoordinatesResponse findById(Long id) {
        return cityCoordinatesMapper.toResponse(getOrThrow(id));
    }

    @Transactional
    public CityCoordinatesResponse create(CityCoordinatesRequest request) {
        CityCoordinates saved = cityCoordinatesRepository.save(cityCoordinatesMapper.toEntity(request));
        return cityCoordinatesMapper.toResponse(saved);
    }

    @Transactional
    public CityCoordinatesResponse update(Long id, CityCoordinatesRequest request) {
        CityCoordinates city = getOrThrow(id);
        city.setCityName(request.cityName());
        city.setState(request.state());
        city.setLatitude(request.latitude());
        city.setLongitude(request.longitude());
        return cityCoordinatesMapper.toResponse(cityCoordinatesRepository.save(city));
    }

    @Transactional
    public void delete(Long id) {
        CityCoordinates city = getOrThrow(id);
        cityCoordinatesRepository.delete(city);
    }

    /**
     * Returns cached coordinates for the given city, fetching and persisting them
     * from the Open-Meteo geocoding API on a cache miss.
     */
    @Transactional
    public CityCoordinates getOrFetch(String cityName) {
        return cityCoordinatesRepository.findByCityNameIgnoreCase(cityName)
                .orElseGet(() -> fetchAndPersist(cityName));
    }

    private CityCoordinates fetchAndPersist(String cityName) {
        GeocodingApiResponse.Result result = geocodingClient.findCity(cityName);

        // The geocoding API's canonical name (e.g. accented) may differ from the
        // user's search term (e.g. "Viamao" vs "Viamão"); re-check the cache under
        // that canonical name before inserting to avoid a duplicate-key violation.
        return cityCoordinatesRepository.findByCityNameIgnoreCase(result.name())
                .orElseGet(() -> cityCoordinatesRepository.save(CityCoordinates.builder()
                        .cityName(result.name())
                        .state(resolveState(result))
                        .latitude(result.latitude())
                        .longitude(result.longitude())
                        .build()));
    }

    private String resolveState(GeocodingApiResponse.Result result) {
        if (result.countryCode() != null && !result.countryCode().isBlank()) {
            return result.countryCode();
        }
        return DEFAULT_STATE;
    }

    private CityCoordinates getOrThrow(Long id) {
        return cityCoordinatesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City coordinates not found: " + id));
    }
}
