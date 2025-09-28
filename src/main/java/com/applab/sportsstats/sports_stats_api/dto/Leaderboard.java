package com.applab.sportsstats.sports_stats_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaderboard {
    private String category;
    private List<LeaderboardEntry> entries;
    private OffsetDateTime lastUpdated;
}
