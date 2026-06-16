package com.vinicius.dev.pulgattiwather.history;

import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchHistoryConsumer {

    private final SearchHistoryRepository searchHistoryRepository;
    private final SearchHistoryMapper searchHistoryMapper;

    @KafkaListener(topics = "${app.kafka.search-history-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(SearchHistoryEvent event) {
        searchHistoryRepository.save(searchHistoryMapper.toEntity(event));
    }
}
