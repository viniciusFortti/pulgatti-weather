package com.vinicius.dev.pulgattiwather.city;

import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesRequest;
import com.vinicius.dev.pulgattiwather.city.dto.CityCoordinatesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityCoordinatesController {

    private final CityCoordinatesService cityCoordinatesService;

    @GetMapping
    public ResponseEntity<List<CityCoordinatesResponse>> findAll() {
        return ResponseEntity.ok(cityCoordinatesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityCoordinatesResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cityCoordinatesService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CityCoordinatesResponse> create(@Valid @RequestBody CityCoordinatesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityCoordinatesService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityCoordinatesResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody CityCoordinatesRequest request) {
        return ResponseEntity.ok(cityCoordinatesService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityCoordinatesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
