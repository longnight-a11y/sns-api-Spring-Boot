package com.example.snsapi.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> items,  // This list can store any types of values
        int total,
        int page,
        int size
) {
}
