package com.applab.sportsstats.sports_stats_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection<T> {
    private List<T> content;
    private PageInfo pageInfo;

    public static <T> Connection<T> from(Page<T> page) {
        return new Connection<>(
            page.getContent(),
            PageInfo.from(page)
        );
    }
}