package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import com.applab.sportsstats.sports_stats_api.entity.Team;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MatchFieldResolver {

    @SchemaMapping(typeName = "Match", field = "winner")
    public Team winner(Match match) {
        return match.getWinner();
    }

    @SchemaMapping(typeName = "Match", field = "isTie")
    public boolean isTie(Match match) {
        return match.isTie();
    }

    @SchemaMapping(typeName = "Match", field = "totalPoints")
    public int totalPoints(Match match) {
        if (match.getHomeTeamScore() == null || match.getAwayTeamScore() == null) return 0;
        return match.getHomeTeamScore() + match.getAwayTeamScore();
    }

    @SchemaMapping(typeName = "Match", field = "isFinished")
    public boolean isFinished(Match match) {
        return match.getStatus() == Match.MatchStatus.COMPLETED;
    }
}
