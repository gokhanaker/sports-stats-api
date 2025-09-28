package com.applab.sportsstats.sports_stats_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "match_date", nullable = false)
    private OffsetDateTime matchDate;
    
    @Column(nullable = false)
    private String venue;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;
    
    @Column(name = "home_team_score")
    private Integer homeTeamScore;
    
    @Column(name = "away_team_score")
    private Integer awayTeamScore;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "match_teams",
        joinColumns = @JoinColumn(name = "match_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @ToString.Exclude
    private List<Team> teams;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    @ToString.Exclude
    private Team homeTeam;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    @ToString.Exclude
    private Team awayTeam;
    
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Stats> stats;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    public enum MatchStatus {
        SCHEDULED,
        LIVE,
        COMPLETED,
        CANCELLED,
        POSTPONED
    }
    
    // Helper method to determine winner
    public Team getWinner() {
        if (status != MatchStatus.COMPLETED || homeTeamScore == null || awayTeamScore == null) {
            return null;
        }
        
        if (homeTeamScore > awayTeamScore) {
            return homeTeam;
        } else if (awayTeamScore > homeTeamScore) {
            return awayTeam;
        }
        
        return null; // Tie game
    }
    
    public boolean isTie() {
        return status == MatchStatus.COMPLETED && 
               homeTeamScore != null && 
               awayTeamScore != null && 
               homeTeamScore.equals(awayTeamScore);
    }
}