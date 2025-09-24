package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import com.applab.sportsstats.sports_stats_api.entity.Team;
import com.applab.sportsstats.sports_stats_api.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamFieldResolver {

    private final MatchRepository matchRepository;

    @SchemaMapping(typeName = "Team", field = "totalWins")
    public int totalWins(Team team) {
        List<Match> matches = matchRepository.findByTeamId(team.getId());
        int wins = 0;
        for (Match m : matches) {
            if (m.getStatus() == Match.MatchStatus.COMPLETED && m.getHomeTeamScore() != null && m.getAwayTeamScore() != null) {
                if (m.getHomeTeamScore() > m.getAwayTeamScore() && m.getHomeTeam().getId().equals(team.getId())) {
                    wins++;
                } else if (m.getAwayTeamScore() > m.getHomeTeamScore() && m.getAwayTeam().getId().equals(team.getId())) {
                    wins++;
                }
            }
        }
        return wins;
    }

    @SchemaMapping(typeName = "Team", field = "totalLosses")
    public int totalLosses(Team team) {
        List<Match> matches = matchRepository.findByTeamId(team.getId());
        int losses = 0;
        for (Match m : matches) {
            if (m.getStatus() == Match.MatchStatus.COMPLETED && m.getHomeTeamScore() != null && m.getAwayTeamScore() != null) {
                if (m.getHomeTeamScore() < m.getAwayTeamScore() && m.getHomeTeam().getId().equals(team.getId())) {
                    losses++;
                } else if (m.getAwayTeamScore() < m.getHomeTeamScore() && m.getAwayTeam().getId().equals(team.getId())) {
                    losses++;
                }
            }
        }
        return losses;
    }

    @SchemaMapping(typeName = "Team", field = "winPercentage")
    public double winPercentage(Team team) {
        int wins = totalWins(team);
        int losses = totalLosses(team);
        int total = wins + losses;
        if (total == 0) return 0.0;
        return (double) wins / total * 100.0;
    }
}
