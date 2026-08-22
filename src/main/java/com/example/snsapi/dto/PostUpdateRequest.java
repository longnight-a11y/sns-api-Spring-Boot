package com.example.snsapi.dto;

import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Size(max = 200) String title,
        String content
) {
}
