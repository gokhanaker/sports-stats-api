package com.applab.sportsstats.sports_stats_api.dto;

import com.applab.sportsstats.sports_stats_api.enums.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PlayerFilter {
    private Long teamId;
    private Position position;
    private Integer minPoints;
    private Integer maxPoints;
    private Integer minAge;
    private Integer maxAge;
    private Integer minJerseyNumber;
    private Integer maxJerseyNumber;
    
    public void validate() {
        if (teamId != null && teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
        
        if (minPoints != null && minPoints < 0) {
            throw new IllegalArgumentException("Minimum points cannot be negative");
        }
        
        if (maxPoints != null && maxPoints < 0) {
            throw new IllegalArgumentException("Maximum points cannot be negative");
        }
        
        if (minPoints != null && maxPoints != null && minPoints > maxPoints) {
            throw new IllegalArgumentException("Minimum points cannot be greater than maximum points");
        }
        
        if (minAge != null && (minAge < 16 || minAge > 50)) {
            throw new IllegalArgumentException("Minimum age must be between 16 and 50");
        }
        
        if (maxAge != null && (maxAge < 16 || maxAge > 50)) {
            throw new IllegalArgumentException("Maximum age must be between 16 and 50");
        }
        
        if (minAge != null && maxAge != null && minAge > maxAge) {
            throw new IllegalArgumentException("Minimum age cannot be greater than maximum age");
        }
        
        if (minJerseyNumber != null && (minJerseyNumber < 0 || minJerseyNumber > 99)) {
            throw new IllegalArgumentException("Jersey number must be between 0 and 99");
        }
        
        if (maxJerseyNumber != null && (maxJerseyNumber < 0 || maxJerseyNumber > 99)) {
            throw new IllegalArgumentException("Jersey number must be between 0 and 99");
        }
        
        if (minJerseyNumber != null && maxJerseyNumber != null && minJerseyNumber > maxJerseyNumber) {
            throw new IllegalArgumentException("Minimum jersey number cannot be greater than maximum");
        }
        
        log.debug("Player filter validation passed: {}", this);
    }
    
    public boolean hasFilters() {
        return teamId != null || position != null || minPoints != null || maxPoints != null 
            || minAge != null || maxAge != null || minJerseyNumber != null || maxJerseyNumber != null;
    }
}