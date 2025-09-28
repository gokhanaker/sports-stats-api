package com.applab.sportsstats.sports_stats_api.dto;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

@Data
@Slf4j
public class MatchFilter {
    private Long teamId;
    private Match.MatchStatus status;
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;
    private String venue;
    private Boolean hasScore; // true = completed matches with scores, false = matches without scores
    
    public void validate() {
        if (teamId != null && teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
        
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("Date from cannot be after date to");
        }
        
        if (venue != null && venue.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue cannot be empty if provided");
        }
        
        if (venue != null && venue.trim().length() < 2) {
            throw new IllegalArgumentException("Venue search must be at least 2 characters");
        }
        
        log.debug("Match filter validation passed: {}", this);
    }
    
    public boolean hasFilters() {
        return teamId != null || status != null || dateFrom != null || dateTo != null 
            || venue != null || hasScore != null;
    }
}