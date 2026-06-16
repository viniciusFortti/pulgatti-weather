package com.vinicius.dev.pulgattiwather.history;

import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchHistoryProducer {

    private final KafkaTemplate<String, SearchHistoryEvent> kafkaTemplate;

    @Value("${app.kafka.search-history-topic}")
    private String topic;

    public void publish(SearchHistoryEvent event) {
        kafkaTemplate.send(topic, event.userId().toString(), event);
    }
}
