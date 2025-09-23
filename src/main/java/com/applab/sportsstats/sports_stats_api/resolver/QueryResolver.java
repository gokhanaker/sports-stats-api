package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.*;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QueryResolver {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final StatsRepository statsRepository;

    // Team Queries
    @QueryMapping
    public List<Team> teams() {
        return teamRepository.findAll();
    }

    @QueryMapping
    public Team team(@Argument Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Team teamByName(@Argument String name) {
        return teamRepository.findByName(name).orElse(null);
    }

    @QueryMapping
    public List<Team> teamsByCity(@Argument String city) {
        return teamRepository.findByCity(city);
    }

    // Player Queries
    @QueryMapping
    public List<Player> players() {
        return playerRepository.findAll();
    }

    @QueryMapping
    public Player player(@Argument Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Player> playersByTeam(@Argument Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    @QueryMapping
    public List<Player> playersByPosition(@Argument Position position) {
        return playerRepository.findByPosition(position);
    }

    // Match Queries
    @QueryMapping
    public List<Match> matches() {
        return matchRepository.findAll();
    }

    @QueryMapping
    public Match match(@Argument Long id) {
        return matchRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Match> liveMatches() {
        return matchRepository.findLiveMatches();
    }

    @QueryMapping
    public List<Match> matchesByTeam(@Argument Long teamId) {
        return matchRepository.findByTeamId(teamId);
    }

    // Stats Queries
    @QueryMapping
    public List<Stats> playerStats(@Argument Long playerId) {
        return statsRepository.findByPlayerId(playerId);
    }

    @QueryMapping
    public List<Stats> matchStats(@Argument Long matchId) {
        return statsRepository.findByMatchId(matchId);
    }
}