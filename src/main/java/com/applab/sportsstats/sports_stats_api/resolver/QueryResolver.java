package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.*;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QueryResolver {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final StatsRepository statsRepository;

    // Team Queries
    @QueryMapping
    public List<Team> teams() {
        log.info("Fetching all teams");
        try {
            List<Team> teams = teamRepository.findAll();
            log.info("Successfully retrieved {} teams", teams.size());
            return teams;
        } catch (Exception e) {
            log.error("Error fetching all teams", e);
            throw new RuntimeException("Unable to fetch teams at this time");
        }
    }

    @QueryMapping
    public Team team(@Argument Long id) {
        log.info("Fetching team with id: {}", id);
        
        if (id == null) {
            log.warn("Team ID cannot be null");
            throw new IllegalArgumentException("Team ID is required");
        }
        
        if (id <= 0) {
            log.warn("Invalid team ID: {}", id);
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
        
        try {
            return teamRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Team not found with id: {}", id);
                        return new RuntimeException("Team not found with id: " + id);
                    });
        } catch (Exception e) {
            log.error("Error fetching team with id: {}", id, e);
            throw new RuntimeException("Unable to fetch team at this time");
        }
    }

    @QueryMapping
    public Team teamByName(@Argument String name) {
        log.info("Fetching team with name: '{}'", name);
        
        if (name == null || name.trim().isEmpty()) {
            log.warn("Team name cannot be null or empty");
            throw new IllegalArgumentException("Team name is required and cannot be empty");
        }
        
        String teamName = name.trim();
        if (teamName.length() > 100) {
            log.warn("Team name too long: '{}'", teamName);
            throw new IllegalArgumentException("Team name cannot be longer than 100 characters");
        }
        
        try {
            return teamRepository.findByName(teamName)
                    .orElseThrow(() -> {
                        log.warn("Team not found with name: '{}'", teamName);
                        return new RuntimeException("Team not found with name: " + teamName);
                    });
        } catch (Exception e) {
            log.error("Error fetching team with name: '{}'", teamName, e);
            throw new RuntimeException("Unable to fetch team at this time");
        }
    }

    @QueryMapping
    public List<Team> teamsByCity(@Argument String city) {
        log.info("Fetching teams in city: '{}'", city);
        
        if (city == null || city.trim().isEmpty()) {
            log.warn("City cannot be null or empty");
            throw new IllegalArgumentException("City is required and cannot be empty");
        }
        
        String cityName = city.trim();
        if (cityName.length() > 100) {
            log.warn("City name too long: '{}'", cityName);
            throw new IllegalArgumentException("City name cannot be longer than 100 characters");
        }
        
        try {
            List<Team> teams = teamRepository.findByCity(cityName);
            log.info("Successfully retrieved {} teams for city: '{}'", teams.size(), cityName);
            return teams;
        } catch (Exception e) {
            log.error("Error fetching teams for city: '{}'", cityName, e);
            throw new RuntimeException("Unable to fetch teams at this time");
        }
    }

    // Player Queries
    @QueryMapping
    public List<Player> players() {
        log.info("Fetching all players");
        try {
            List<Player> players = playerRepository.findAll();
            log.info("Successfully retrieved {} players", players.size());
            return players;
        } catch (Exception e) {
            log.error("Error fetching all players", e);
            throw new RuntimeException("Unable to fetch players at this time");
        }
    }

    @QueryMapping
    public Player player(@Argument Long id) {
        log.info("Fetching player with id: {}", id);
        
        if (id == null) {
            log.warn("Player ID cannot be null");
            throw new IllegalArgumentException("Player ID is required");
        }
        
        if (id <= 0) {
            log.warn("Invalid player ID: {}", id);
            throw new IllegalArgumentException("Player ID must be a positive number");
        }
        
        try {
            return playerRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Player not found with id: {}", id);
                        return new RuntimeException("Player not found with id: " + id);
                    });
        } catch (Exception e) {
            log.error("Error fetching player with id: {}", id, e);
            throw new RuntimeException("Unable to fetch player at this time");
        }
    }

    @QueryMapping
    public List<Player> playersByTeam(@Argument Long teamId) {
        log.info("Fetching players for team id: {}", teamId);
        
        if (teamId == null) {
            log.warn("Team ID cannot be null for player lookup");
            throw new IllegalArgumentException("Team ID is required");
        }
        
        if (teamId <= 0) {
            log.warn("Invalid team ID for player lookup: {}", teamId);
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
        
        try {
            // Verify team exists
            boolean teamExists = teamRepository.existsById(teamId);
            if (!teamExists) {
                log.warn("Team not found with id: {} for player lookup", teamId);
                throw new RuntimeException("Team not found with id: " + teamId);
            }
            
            List<Player> players = playerRepository.findByTeamId(teamId);
            log.info("Successfully retrieved {} players for team id: {}", players.size(), teamId);
            return players;
        } catch (Exception e) {
            log.error("Error fetching players for team id: {}", teamId, e);
            throw new RuntimeException("Unable to fetch players at this time");
        }
    }

    @QueryMapping
    public List<Player> playersByPosition(@Argument Position position) {
        log.info("Fetching players with position: {}", position);
        
        if (position == null) {
            log.warn("Position cannot be null");
            throw new IllegalArgumentException("Position is required");
        }
        
        try {
            List<Player> players = playerRepository.findByPosition(position);
            log.info("Successfully retrieved {} players for position: {}", players.size(), position);
            return players;
        } catch (Exception e) {
            log.error("Error fetching players for position: {}", position, e);
            throw new RuntimeException("Unable to fetch players at this time");
        }
    }

    // Match Queries
    @QueryMapping
    public List<Match> matches() {
        log.info("Fetching all matches");
        try {
            List<Match> matches = matchRepository.findAll();
            log.info("Successfully retrieved {} matches", matches.size());
            return matches;
        } catch (Exception e) {
            log.error("Error fetching all matches", e);
            throw new RuntimeException("Unable to fetch matches at this time");
        }
    }

    @QueryMapping
    public Match match(@Argument Long id) {
        log.info("Fetching match with id: {}", id);
        
        if (id == null) {
            log.warn("Match ID cannot be null");
            throw new IllegalArgumentException("Match ID is required");
        }
        
        if (id <= 0) {
            log.warn("Invalid match ID: {}", id);
            throw new IllegalArgumentException("Match ID must be a positive number");
        }
        
        try {
            return matchRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Match not found with id: {}", id);
                        return new RuntimeException("Match not found with id: " + id);
                    });
        } catch (Exception e) {
            log.error("Error fetching match with id: {}", id, e);
            throw new RuntimeException("Unable to fetch match at this time");
        }
    }

    @QueryMapping
    public List<Match> liveMatches() {
        log.info("Fetching live matches");
        try {
            List<Match> liveMatches = matchRepository.findLiveMatches();
            log.info("Successfully retrieved {} live matches", liveMatches.size());
            return liveMatches;
        } catch (Exception e) {
            log.error("Error fetching live matches", e);
            throw new RuntimeException("Unable to fetch live matches at this time");
        }
    }

    @QueryMapping
    public List<Match> matchesByTeam(@Argument Long teamId) {
        log.info("Fetching matches for team id: {}", teamId);
        
        if (teamId == null) {
            log.warn("Team ID cannot be null for match lookup");
            throw new IllegalArgumentException("Team ID is required");
        }
        
        if (teamId <= 0) {
            log.warn("Invalid team ID for match lookup: {}", teamId);
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
        
        try {
            // Verify team exists
            boolean teamExists = teamRepository.existsById(teamId);
            if (!teamExists) {
                log.warn("Team not found with id: {} for match lookup", teamId);
                throw new RuntimeException("Team not found with id: " + teamId);
            }
            
            List<Match> matches = matchRepository.findByTeamId(teamId);
            log.info("Successfully retrieved {} matches for team id: {}", matches.size(), teamId);
            return matches;
        } catch (Exception e) {
            log.error("Error fetching matches for team id: {}", teamId, e);
            throw new RuntimeException("Unable to fetch matches at this time");
        }
    }

    // Stats Queries
    @QueryMapping
    public List<Stats> playerStats(@Argument Long playerId) {
        log.info("Fetching stats for player with id: {}", playerId);
        
        if (playerId == null) {
            log.warn("Player ID cannot be null for stats lookup");
            throw new IllegalArgumentException("Player ID is required");
        }
        
        if (playerId <= 0) {
            log.warn("Invalid player ID for stats lookup: {}", playerId);
            throw new IllegalArgumentException("Player ID must be a positive number");
        }
        
        try {
            // Verify player exists
            boolean playerExists = playerRepository.existsById(playerId);
            if (!playerExists) {
                log.warn("Player not found with id: {} for stats lookup", playerId);
                throw new RuntimeException("Player not found with id: " + playerId);
            }
            
            List<Stats> playerStats = statsRepository.findByPlayerId(playerId);
            log.info("Successfully retrieved {} stats records for player id: {}", playerStats.size(), playerId);
            return playerStats;
        } catch (Exception e) {
            log.error("Error fetching stats for player id: {}", playerId, e);
            throw new RuntimeException("Unable to fetch player stats at this time");
        }
    }

    @QueryMapping
    public List<Stats> matchStats(@Argument Long matchId) {
        log.info("Fetching stats for match with id: {}", matchId);
        
        if (matchId == null) {
            log.warn("Match ID cannot be null for stats lookup");
            throw new IllegalArgumentException("Match ID is required");
        }
        
        if (matchId <= 0) {
            log.warn("Invalid match ID for stats lookup: {}", matchId);
            throw new IllegalArgumentException("Match ID must be a positive number");
        }
        
        try {
            // Verify match exists
            boolean matchExists = matchRepository.existsById(matchId);
            if (!matchExists) {
                log.warn("Match not found with id: {} for stats lookup", matchId);
                throw new RuntimeException("Match not found with id: " + matchId);
            }
            
            List<Stats> matchStats = statsRepository.findByMatchId(matchId);
            log.info("Successfully retrieved {} stats records for match id: {}", matchStats.size(), matchId);
            return matchStats;
        } catch (Exception e) {
            log.error("Error fetching stats for match id: {}", matchId, e);
            throw new RuntimeException("Unable to fetch match stats at this time");
        }
    }

    // Search functionality
    @QueryMapping
    public List<Player> searchPlayers(@Argument String name) {
        log.info("Searching players with name: '{}'", name);
        
        if (name == null || name.trim().isEmpty()) {
            log.warn("Search name cannot be null or empty");
            throw new IllegalArgumentException("Search name is required and cannot be empty");
        }

        String searchTerm = name.trim();
        
        if (searchTerm.length() < 2) {
            log.warn("Search term too short: '{}'", searchTerm);
            throw new IllegalArgumentException("Search term must be at least 2 characters long");
        }
        
        if (searchTerm.length() > 50) {
            log.warn("Search term too long: '{}'", searchTerm);
            throw new IllegalArgumentException("Search term cannot be longer than 50 characters");
        }
        
        try {
            // Use the available search method that searches both first and last names
            List<Player> players = playerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    searchTerm, searchTerm);
            
            log.info("Search for '{}' returned {} players", searchTerm, players.size());
            return players;
        } catch (Exception e) {
            log.error("Error searching for players with name: '{}'", searchTerm, e);
            throw new RuntimeException("Unable to search players at this time");
        }
    }
}