package com.vinicius.dev.pulgattiwather.history;

import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryEvent;
import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryResponse;
import org.springframework.stereotype.Component;

@Component
public class SearchHistoryMapper {

    public SearchHistoryResponse toResponse(SearchHistory history) {
        return new SearchHistoryResponse(
                history.getId(),
                history.getSearchedCity(),
                history.getSearchedAt(),
                history.getSnapshotTemperature(),
                history.getSnapshotWeatherCode());
    }

    public SearchHistory toEntity(SearchHistoryEvent event) {
        return SearchHistory.builder()
                .userId(event.userId())
                .searchedCity(event.searchedCity())
                .searchedAt(event.searchedAt())
                .snapshotTemperature(event.snapshotTemperature())
                .snapshotWeatherCode(event.snapshotWeatherCode())
                .build();
    }
}
