package com.applab.sportsstats.sports_stats_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @ToString.Exclude
    private Match match;

    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer assists = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer rebounds = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer steals = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer blocks = 0;
    
    @Column(name = "field_goals_made", nullable = false)
    @Builder.Default
    private Integer fieldGoalsMade = 0;
    
    @Column(name = "field_goals_attempted", nullable = false)
    @Builder.Default
    private Integer fieldGoalsAttempted = 0;
    
    @Column(name = "three_pointers_made", nullable = false)
    @Builder.Default
    private Integer threePointersMade = 0;
    
    @Column(name = "three_pointers_attempted", nullable = false)
    @Builder.Default
    private Integer threePointersAttempted = 0;
    
    @Column(name = "free_throws_made", nullable = false)
    @Builder.Default
    private Integer freeThrowsMade = 0;
    
    @Column(name = "free_throws_attempted", nullable = false)
    @Builder.Default
    private Integer freeThrowsAttempted = 0;
    
    @Column(name = "minutes_played")
    private Integer minutesPlayed;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    // Helper methods for calculated stats
    public Double getFieldGoalPercentage() {
        if (fieldGoalsAttempted == 0) return 0.0;
        return (double) fieldGoalsMade / fieldGoalsAttempted * 100;
    }
    
    public Double getThreePointPercentage() {
        if (threePointersAttempted == 0) return 0.0;
        return (double) threePointersMade / threePointersAttempted * 100;
    }
    
    public Double getFreeThrowPercentage() {
        if (freeThrowsAttempted == 0) return 0.0;
        return (double) freeThrowsMade / freeThrowsAttempted * 100;
    }
}