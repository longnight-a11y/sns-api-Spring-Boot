package com.example.snsapi.dto;

import java.util.UUID;

public record PostResponse(
        UUID id,
        String title,
        String content,
        UserResponse user
) {
}
