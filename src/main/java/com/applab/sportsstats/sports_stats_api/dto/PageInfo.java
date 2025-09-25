package com.applab.sportsstats.sports_stats_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;

    public static PageInfo from(Page<?> page) {
        return new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getNumber(),
            page.getSize()
        );
    }
}