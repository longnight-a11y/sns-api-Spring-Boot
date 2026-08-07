package com.example.snsapi.dto;

import java.util.UUID;

public record LoginResponse(
        String token,
        String tokenType
) {
}
