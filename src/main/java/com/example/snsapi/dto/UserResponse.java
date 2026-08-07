package com.example.snsapi.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username
) {
}
