package com.vinicius.dev.pulgattiwather.city;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cities_coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false, length = 100)
    private String cityName;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal longitude;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}
