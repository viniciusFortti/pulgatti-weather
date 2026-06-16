package com.vinicius.dev.pulgattiwather.history;

import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ResponseEntity<List<SearchHistoryResponse>> findCurrentUserHistory() {
        return ResponseEntity.ok(searchHistoryService.findCurrentUserHistory());
    }
}
