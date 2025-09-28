package com.applab.sportsstats.sports_stats_api.dto;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {
    private Player player;
    private Double value;
    private Integer rank;
}
