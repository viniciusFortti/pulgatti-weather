package com.vinicius.dev.pulgattiwather.user.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
