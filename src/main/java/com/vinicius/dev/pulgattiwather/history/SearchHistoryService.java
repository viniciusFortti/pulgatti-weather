package com.vinicius.dev.pulgattiwather.history;

import com.vinicius.dev.pulgattiwather.common.exception.ResourceNotFoundException;
import com.vinicius.dev.pulgattiwather.history.dto.SearchHistoryResponse;
import com.vinicius.dev.pulgattiwather.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final SearchHistoryMapper searchHistoryMapper;
    private final UserRepository userRepository;

    public List<SearchHistoryResponse> findCurrentUserHistory() {
        Long userId = currentUserId();

        return searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId).stream()
                .map(searchHistoryMapper::toResponse)
                .toList();
    }

    private Long currentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email))
                .getId();
    }
}
