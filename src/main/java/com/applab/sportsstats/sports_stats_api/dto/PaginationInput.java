package com.applab.sportsstats.sports_stats_api.dto;

import lombok.Data;

@Data
public class PaginationInput {
    private Integer page = 0;
    private Integer size = 10;
    
    public void validate() {
        if (page != null && page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size != null && (size <= 0 || size > 100)) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
    
    public int getValidatedPage() {
        return page != null ? Math.max(0, page) : 0;
    }
    
    public int getValidatedSize() {
        if (size == null) return 10;
        return Math.min(Math.max(1, size), 100);
    }
}