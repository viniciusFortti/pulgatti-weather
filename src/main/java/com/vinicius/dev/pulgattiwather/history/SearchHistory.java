package com.vinicius.dev.pulgattiwather.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "searched_city", nullable = false, length = 100)
    private String searchedCity;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;

    @Column(name = "snapshot_temperature", precision = 4, scale = 1)
    private BigDecimal snapshotTemperature;

    @Column(name = "snapshot_weather_code")
    private Integer snapshotWeatherCode;

    @PrePersist
    private void onCreate() {
        if (this.searchedAt == null) {
            this.searchedAt = LocalDateTime.now();
        }
    }
}
