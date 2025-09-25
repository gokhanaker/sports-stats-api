package com.applab.sportsstats.sports_stats_api.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TeamFilter {
    private String city;
    private Integer minFoundedYear;
    private Integer maxFoundedYear;
    private String nameContains;
    private String coachName;
    
    public void validate() {
        if (minFoundedYear != null && (minFoundedYear < 1800 || minFoundedYear > 2030)) {
            throw new IllegalArgumentException("Founded year must be between 1800 and 2030");
        }
        
        if (maxFoundedYear != null && (maxFoundedYear < 1800 || maxFoundedYear > 2030)) {
            throw new IllegalArgumentException("Founded year must be between 1800 and 2030");
        }
        
        if (minFoundedYear != null && maxFoundedYear != null && minFoundedYear > maxFoundedYear) {
            throw new IllegalArgumentException("Minimum founded year cannot be greater than maximum");
        }
        
        if (city != null && city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty if provided");
        }
        
        if (nameContains != null && nameContains.trim().length() < 2) {
            throw new IllegalArgumentException("Name search must be at least 2 characters");
        }
        
        if (coachName != null && coachName.trim().length() < 2) {
            throw new IllegalArgumentException("Coach name search must be at least 2 characters");
        }
        
        log.debug("Team filter validation passed: {}", this);
    }
    
    public boolean hasFilters() {
        return city != null || minFoundedYear != null || maxFoundedYear != null 
            || nameContains != null || coachName != null;
    }
}