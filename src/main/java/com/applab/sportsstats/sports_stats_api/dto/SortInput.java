package com.applab.sportsstats.sports_stats_api.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class SortInput {
    private String field;
    private SortDirection direction = SortDirection.ASC;
    
    public enum SortDirection {
        ASC, DESC
    }
    
    public void validate(String entityType) {
        if (field == null || field.trim().isEmpty()) {
            throw new IllegalArgumentException("Sort field cannot be empty");
        }
        
        // Validate allowed sort fields per entity type
        switch (entityType.toLowerCase()) {
            case "team":
                if (!isValidTeamSortField(field)) {
                    throw new IllegalArgumentException("Invalid sort field for Team: " + field);
                }
                break;
            case "player":
                if (!isValidPlayerSortField(field)) {
                    throw new IllegalArgumentException("Invalid sort field for Player: " + field);
                }
                break;
            case "match":
                if (!isValidMatchSortField(field)) {
                    throw new IllegalArgumentException("Invalid sort field for Match: " + field);
                }
                break;
            case "stats":
                if (!isValidStatsSortField(field)) {
                    throw new IllegalArgumentException("Invalid sort field for Stats: " + field);
                }
                break;
        }
    }
    
    private boolean isValidTeamSortField(String field) {
        return field.matches("^(id|name|city|foundedYear|coachName|homeStadium)$");
    }
    
    private boolean isValidPlayerSortField(String field) {
        return field.matches("^(id|firstName|lastName|jerseyNumber|position|birthDate)$");
    }
    
    private boolean isValidMatchSortField(String field) {
        return field.matches("^(id|matchDate|venue|status|homeTeamScore|awayTeamScore)$");
    }
    
    private boolean isValidStatsSortField(String field) {
        return field.matches("^(id|points|assists|rebounds|steals|blocks)$");
    }
    
    public Sort toSpringSort() {
        if (field == null) {
            return Sort.by("id").ascending();
        }
        
        Sort.Direction springDirection = direction == SortDirection.DESC ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
            
        return Sort.by(springDirection, field);
    }
}