package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.dto.*;
import com.applab.sportsstats.sports_stats_api.entity.*;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    // ========== PAGINATED QUERIES ==========

    @QueryMapping
    public Connection<Team> teamsPaginated(@Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching teams with pagination: {}, sort: {}", pagination, sort);
        
        try {
            // Apply defaults and validate
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("team");
            
            // Create pageable
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("id").ascending()
            );
            
            Page<Team> teamPage = teamRepository.findAll(pageable);
            Connection<Team> connection = Connection.from(teamPage);
            
            log.info("Successfully retrieved {} teams (page {}/{})", 
                    teamPage.getContent().size(), 
                    teamPage.getNumber() + 1, 
                    teamPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching paginated teams", e);
            throw new RuntimeException("Unable to fetch teams at this time");
        }
    }

    @QueryMapping
    public Connection<Player> playersPaginated(@Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching players with pagination: {}, sort: {}", pagination, sort);
        
        try {
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("player");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("id").ascending()
            );
            
            Page<Player> playerPage = playerRepository.findAll(pageable);
            Connection<Player> connection = Connection.from(playerPage);
            
            log.info("Successfully retrieved {} players (page {}/{})", 
                    playerPage.getContent().size(), 
                    playerPage.getNumber() + 1, 
                    playerPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching paginated players", e);
            throw new RuntimeException("Unable to fetch players at this time");
        }
    }

    @QueryMapping
    public Connection<Match> matchesPaginated(@Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching matches with pagination: {}, sort: {}", pagination, sort);
        
        try {
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("match");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("matchDate").descending()
            );
            
            Page<Match> matchPage = matchRepository.findAll(pageable);
            Connection<Match> connection = Connection.from(matchPage);
            
            log.info("Successfully retrieved {} matches (page {}/{})", 
                    matchPage.getContent().size(), 
                    matchPage.getNumber() + 1, 
                    matchPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching paginated matches", e);
            throw new RuntimeException("Unable to fetch matches at this time");
        }
    }

    @QueryMapping
    public Connection<Stats> statsPaginated(@Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching stats with pagination: {}, sort: {}", pagination, sort);
        
        try {
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("stats");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("points").descending()
            );
            
            Page<Stats> statsPage = statsRepository.findAll(pageable);
            Connection<Stats> connection = Connection.from(statsPage);
            
            log.info("Successfully retrieved {} stats records (page {}/{})", 
                    statsPage.getContent().size(), 
                    statsPage.getNumber() + 1, 
                    statsPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching paginated stats", e);
            throw new RuntimeException("Unable to fetch stats at this time");
        }
    }

    // ========== PAGINATED SEARCH & FILTERING ==========

    @QueryMapping
    public Connection<Player> searchPlayersPaginated(@Argument String name, @Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Searching players with pagination. Name: '{}', pagination: {}, sort: {}", name, pagination, sort);
        
        // Validate search term
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name is required and cannot be empty");
        }
        
        String searchTerm = name.trim();
        if (searchTerm.length() < 2) {
            throw new IllegalArgumentException("Search term must be at least 2 characters long");
        }
        if (searchTerm.length() > 50) {
            throw new IllegalArgumentException("Search term cannot be longer than 50 characters");
        }
        
        try {
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("player");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("lastName", "firstName").ascending()
            );
            
            Page<Player> playerPage = playerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    searchTerm, searchTerm, pageable);
            Connection<Player> connection = Connection.from(playerPage);
            
            log.info("Search for '{}' returned {} players (page {}/{})", 
                    searchTerm, 
                    playerPage.getContent().size(), 
                    playerPage.getNumber() + 1, 
                    playerPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error searching players with pagination. Search term: '{}'", searchTerm, e);
            throw new RuntimeException("Unable to search players at this time");
        }
    }

    @QueryMapping
    public Connection<Player> playersByTeamPaginated(@Argument Long teamId, @Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching players by team with pagination. TeamId: {}, pagination: {}, sort: {}", teamId, pagination, sort);
        
        // Validate team ID
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
        
        try {
            // Verify team exists
            if (!teamRepository.existsById(teamId)) {
                throw new RuntimeException("Team not found with id: " + teamId);
            }
            
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("player");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("jerseyNumber").ascending()
            );
            
            Page<Player> playerPage = playerRepository.findByTeamId(teamId, pageable);
            Connection<Player> connection = Connection.from(playerPage);
            
            log.info("Successfully retrieved {} players for team {} (page {}/{})", 
                    playerPage.getContent().size(), 
                    teamId,
                    playerPage.getNumber() + 1, 
                    playerPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching players by team with pagination. TeamId: {}", teamId, e);
            throw new RuntimeException("Unable to fetch players at this time");
        }
    }

    @QueryMapping
    public Connection<Match> matchesByTeamPaginated(@Argument Long teamId, @Argument PaginationInput pagination, @Argument SortInput sort) {
        log.info("Fetching matches by team with pagination. TeamId: {}, pagination: {}, sort: {}", teamId, pagination, sort);
        
        // Validate team ID
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
        
        try {
            // Verify team exists
            if (!teamRepository.existsById(teamId)) {
                throw new RuntimeException("Team not found with id: " + teamId);
            }
            
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("match");
            
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("matchDate").descending()
            );
            
            Page<Match> matchPage = matchRepository.findByTeamId(teamId, pageable);
            Connection<Match> connection = Connection.from(matchPage);
            
            log.info("Successfully retrieved {} matches for team {} (page {}/{})", 
                    matchPage.getContent().size(), 
                    teamId,
                    matchPage.getNumber() + 1, 
                    matchPage.getTotalPages());
            
            return connection;
        } catch (Exception e) {
            log.error("Error fetching matches by team with pagination. TeamId: {}", teamId, e);
            throw new RuntimeException("Unable to fetch matches at this time");
        }
    }

    // ========== FILTERED QUERIES ==========

    @QueryMapping
    public Connection<Player> playersFiltered(
            @Argument PlayerFilter filter, 
            @Argument PaginationInput pagination, 
            @Argument SortInput sort) {
        
        log.info("Fetching players with filter: {}, pagination: {}, sort: {}", filter, pagination, sort);
        
        try {
            // Set defaults and validate
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("player");
            if (filter != null) filter.validate();
            
            // Create pageable
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("id").ascending()
            );
            
            Page<Player> playerPage;
            
            if (filter == null || !filter.hasFilters()) {
                // No filters - use simple findAll
                log.debug("No filters applied, fetching all players");
                playerPage = playerRepository.findAll(pageable);
            } else if (filter.getMinPoints() != null || filter.getMaxPoints() != null) {
                // Advanced filtering with stats aggregation
                log.debug("Using advanced filtering with stats: minPoints={}, maxPoints={}", 
                         filter.getMinPoints(), filter.getMaxPoints());
                playerPage = playerRepository.findWithAdvancedFilters(
                    filter.getTeamId(),
                    filter.getPosition(),
                    filter.getMinJerseyNumber(),
                    filter.getMaxJerseyNumber(),
                    filter.getMinPoints(),
                    filter.getMaxPoints(),
                    pageable
                );
            } else {
                // Basic filtering without stats
                log.debug("Using basic filtering: teamId={}, position={}, jerseyRange={}-{}", 
                         filter.getTeamId(), filter.getPosition(), 
                         filter.getMinJerseyNumber(), filter.getMaxJerseyNumber());
                playerPage = playerRepository.findWithBasicFilters(
                    filter.getTeamId(),
                    filter.getPosition(),
                    filter.getMinJerseyNumber(),
                    filter.getMaxJerseyNumber(),
                    pageable
                );
            }
            
            Connection<Player> connection = Connection.from(playerPage);
            
            log.info("Filter returned {} players (page {}/{})", 
                    playerPage.getContent().size(), 
                    playerPage.getNumber() + 1, 
                    playerPage.getTotalPages());
            
            return connection;
            
        } catch (Exception e) {
            log.error("Error fetching filtered players", e);
            throw new RuntimeException("Unable to fetch players with the specified filters");
        }
    }

    @QueryMapping
    public Connection<Match> matchesFiltered(
            @Argument MatchFilter filter, 
            @Argument PaginationInput pagination, 
            @Argument SortInput sort) {
        
        log.info("Fetching matches with filter: {}, pagination: {}, sort: {}", filter, pagination, sort);
        
        try {
            // Set defaults and validate
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("match");
            if (filter != null) filter.validate();
            
            // Create pageable
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("matchDate").descending()
            );
            
            Page<Match> matchPage;
            
            if (filter == null || !filter.hasFilters()) {
                // No filters - use simple findAll
                log.debug("No filters applied, fetching all matches");
                matchPage = matchRepository.findAll(pageable);
            } else {
                // Apply filters
                log.debug("Using match filtering: teamId={}, status={}, dateRange={} to {}, venue={}, hasScore={}", 
                         filter.getTeamId(), filter.getStatus(), filter.getDateFrom(), 
                         filter.getDateTo(), filter.getVenue(), filter.getHasScore());
                matchPage = matchRepository.findWithFilters(
                    filter.getTeamId(),
                    filter.getStatus(),
                    filter.getDateFrom(),
                    filter.getDateTo(),
                    filter.getVenue(),
                    filter.getHasScore(),
                    pageable
                );
            }
            
            Connection<Match> connection = Connection.from(matchPage);
            
            log.info("Filter returned {} matches (page {}/{})", 
                    matchPage.getContent().size(), 
                    matchPage.getNumber() + 1, 
                    matchPage.getTotalPages());
            
            return connection;
            
        } catch (Exception e) {
            log.error("Error fetching filtered matches", e);
            throw new RuntimeException("Unable to fetch matches with the specified filters");
        }
    }

    @QueryMapping
    public Connection<Team> teamsFiltered(
            @Argument TeamFilter filter, 
            @Argument PaginationInput pagination, 
            @Argument SortInput sort) {
        
        log.info("Fetching teams with filter: {}, pagination: {}, sort: {}", filter, pagination, sort);
        
        try {
            // Set defaults and validate
            if (pagination == null) pagination = new PaginationInput();
            pagination.validate();
            
            if (sort != null) sort.validate("team");
            if (filter != null) filter.validate();
            
            // Create pageable
            Pageable pageable = PageRequest.of(
                pagination.getValidatedPage(),
                pagination.getValidatedSize(),
                sort != null ? sort.toSpringSort() : org.springframework.data.domain.Sort.by("name").ascending()
            );
            
            Page<Team> teamPage;
            
            if (filter == null || !filter.hasFilters()) {
                // No filters - use simple findAll
                log.debug("No filters applied, fetching all teams");
                teamPage = teamRepository.findAll(pageable);
            } else {
                // Apply filters  
                log.debug("Using team filtering: city={}, foundedYearRange={}-{}, nameContains={}, coachName={}", 
                         filter.getCity(), filter.getMinFoundedYear(), 
                         filter.getMaxFoundedYear(), filter.getNameContains(), filter.getCoachName());
                teamPage = teamRepository.findWithFilters(
                    filter.getCity(),
                    filter.getMinFoundedYear(),
                    filter.getMaxFoundedYear(),
                    filter.getNameContains(),
                    filter.getCoachName(),
                    pageable
                );
            }
            
            Connection<Team> connection = Connection.from(teamPage);
            
            log.info("Filter returned {} teams (page {}/{})", 
                    teamPage.getContent().size(), 
                    teamPage.getNumber() + 1, 
                    teamPage.getTotalPages());
            
            return connection;
            
        } catch (Exception e) {
            log.error("Error fetching filtered teams", e);
            throw new RuntimeException("Unable to fetch teams with the specified filters");
        }
    }

    // Leaderboard Queries
    @QueryMapping
    public Leaderboard pointsLeaderboard(@Argument Integer limit) {
        log.info("Fetching points leaderboard with limit: {}", limit);
        try {
            List<Object[]> results = statsRepository.findTotalPointsLeaderboard();
            List<LeaderboardEntry> entries = new ArrayList<>();
            
            int maxEntries = limit != null ? Math.min(limit, results.size()) : results.size();
            int rank = 1;
            
            for (int i = 0; i < maxEntries; i++) {
                Object[] row = results.get(i);
                Long playerId = (Long) row[0];
                String firstName = (String) row[1];
                String lastName = (String) row[2];
                Double totalPoints = ((Number) row[3]).doubleValue();
                
                // Fetch the full player object
                Player player = playerRepository.findById(playerId).orElse(null);
                if (player != null) {
                    entries.add(new LeaderboardEntry(player, totalPoints, rank++));
                }
            }
            
            return new Leaderboard("Points", entries, OffsetDateTime.now(ZoneOffset.UTC));
            
        } catch (Exception e) {
            log.error("Error fetching points leaderboard", e);
            throw new RuntimeException("Unable to fetch points leaderboard");
        }
    }

    @QueryMapping
    public Leaderboard assistsLeaderboard(@Argument Integer limit) {
        log.info("Fetching assists leaderboard with limit: {}", limit);
        try {
            List<Object[]> results = statsRepository.findTotalAssistsLeaderboard();
            List<LeaderboardEntry> entries = new ArrayList<>();
            
            int maxEntries = limit != null ? Math.min(limit, results.size()) : results.size();
            int rank = 1;
            
            for (int i = 0; i < maxEntries; i++) {
                Object[] row = results.get(i);
                Long playerId = (Long) row[0];
                String firstName = (String) row[1];
                String lastName = (String) row[2];
                Double totalAssists = ((Number) row[3]).doubleValue();
                
                // Fetch the full player object
                Player player = playerRepository.findById(playerId).orElse(null);
                if (player != null) {
                    entries.add(new LeaderboardEntry(player, totalAssists, rank++));
                }
            }
            
            return new Leaderboard("Assists", entries, OffsetDateTime.now(ZoneOffset.UTC));
            
        } catch (Exception e) {
            log.error("Error fetching assists leaderboard", e);
            throw new RuntimeException("Unable to fetch assists leaderboard");
        }
    }

    @QueryMapping
    public Leaderboard reboundsLeaderboard(@Argument Integer limit) {
        log.info("Fetching rebounds leaderboard with limit: {}", limit);
        try {
            List<Object[]> results = statsRepository.findTotalReboundsLeaderboard();
            List<LeaderboardEntry> entries = new ArrayList<>();
            
            int maxEntries = limit != null ? Math.min(limit, results.size()) : results.size();
            int rank = 1;
            
            for (int i = 0; i < maxEntries; i++) {
                Object[] row = results.get(i);
                Long playerId = (Long) row[0];
                String firstName = (String) row[1];
                String lastName = (String) row[2];
                Double totalRebounds = ((Number) row[3]).doubleValue();
                
                // Fetch the full player object
                Player player = playerRepository.findById(playerId).orElse(null);
                if (player != null) {
                    entries.add(new LeaderboardEntry(player, totalRebounds, rank++));
                }
            }
            
            return new Leaderboard("Rebounds", entries, OffsetDateTime.now(ZoneOffset.UTC));
            
        } catch (Exception e) {
            log.error("Error fetching rebounds leaderboard", e);
            throw new RuntimeException("Unable to fetch rebounds leaderboard");
        }
    }

    @QueryMapping
    public List<Match> upcomingMatches() {
        log.info("Fetching upcoming matches");
        try {
            OffsetDateTime currentDate = OffsetDateTime.now(ZoneOffset.UTC);
            List<Match> upcomingMatches = matchRepository.findUpcomingMatches(currentDate);
            log.info("Successfully retrieved {} upcoming matches", upcomingMatches.size());
            return upcomingMatches;
        } catch (Exception e) {
            log.error("Error fetching upcoming matches", e);
            throw new RuntimeException("Unable to fetch upcoming matches");
        }
    }
}