package com.example.snsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(max = 50, min = 1) String username,
        @NotBlank @Size(min = 8) String password
) {
}
