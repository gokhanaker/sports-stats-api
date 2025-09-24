package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.entity.Stats;
import com.applab.sportsstats.sports_stats_api.entity.Team;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.repository.MatchRepository;
import com.applab.sportsstats.sports_stats_api.repository.PlayerRepository;
import com.applab.sportsstats.sports_stats_api.repository.StatsRepository;
import com.applab.sportsstats.sports_stats_api.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Transactional
public class MutationResolver {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final StatsRepository statsRepository;

    // ==================== TEAM MUTATIONS ====================

    @MutationMapping
    public Team createTeam(@Argument("input") @Valid CreateTeamInput input) {
        Team team = Team.builder()
                .name(input.name())
                .city(input.city())
                .foundedYear(input.foundedYear())
                .coachName(input.coachName())
                .homeStadium(input.homeStadium())
                .build();
        return teamRepository.save(team);
    }

    @MutationMapping
    public Team updateTeam(@Argument("input") @Valid UpdateTeamInput input) {
        Team team = teamRepository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + input.id()));
        
        Optional.ofNullable(input.name()).ifPresent(team::setName);
        Optional.ofNullable(input.city()).ifPresent(team::setCity);
        Optional.ofNullable(input.foundedYear()).ifPresent(team::setFoundedYear);
        Optional.ofNullable(input.coachName()).ifPresent(team::setCoachName);
        Optional.ofNullable(input.homeStadium()).ifPresent(team::setHomeStadium);
        
        return teamRepository.save(team);
    }

    @MutationMapping
    public Boolean deleteTeam(@Argument Long id) {
        if (!teamRepository.existsById(id)) {
            return false;
        }
        teamRepository.deleteById(id);
        return true;
    }

    // ==================== PLAYER MUTATIONS ====================

    @MutationMapping
    public Player createPlayer(@Argument("input") @Valid CreatePlayerInput input) {
        Team team = teamRepository.findById(input.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + input.teamId()));

        // Check if jersey number is already taken by another player in the same team
        boolean jerseyTaken = playerRepository.findByTeamId(input.teamId())
                .stream()
                .anyMatch(p -> p.getJerseyNumber().equals(input.jerseyNumber()));
        
        if (jerseyTaken) {
            throw new IllegalArgumentException("Jersey number " + input.jerseyNumber() + " is already taken by another player in this team");
        }

        Player player = Player.builder()
                .firstName(input.firstName())
                .lastName(input.lastName())
                .jerseyNumber(input.jerseyNumber())
                .position(input.position())
                .dateOfBirth(input.dateOfBirth())
                .height(input.height())
                .weight(input.weight())
                .team(team)
                .build();
        
        return playerRepository.save(player);
    }

    @MutationMapping
    public Player updatePlayer(@Argument("input") @Valid UpdatePlayerInput input) {
        Player player = playerRepository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + input.id()));

        Optional.ofNullable(input.firstName()).ifPresent(player::setFirstName);
        Optional.ofNullable(input.lastName()).ifPresent(player::setLastName);
        Optional.ofNullable(input.jerseyNumber()).ifPresent(player::setJerseyNumber);
        Optional.ofNullable(input.position()).ifPresent(player::setPosition);
        Optional.ofNullable(input.dateOfBirth()).ifPresent(player::setDateOfBirth);
        Optional.ofNullable(input.height()).ifPresent(player::setHeight);
        Optional.ofNullable(input.weight()).ifPresent(player::setWeight);
        
        if (input.teamId() != null) {
            Team team = teamRepository.findById(input.teamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + input.teamId()));
            player.setTeam(team);
        }
        
        return playerRepository.save(player);
    }

    @MutationMapping
    public Boolean deletePlayer(@Argument Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }
        playerRepository.deleteById(id);
        return true;
    }

    // ==================== MATCH MUTATIONS ====================

    @MutationMapping
    public Match createMatch(@Argument("input") @Valid CreateMatchInput input) {
        Team homeTeam = teamRepository.findById(input.homeTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Home team not found with id: " + input.homeTeamId()));
        Team awayTeam = teamRepository.findById(input.awayTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Away team not found with id: " + input.awayTeamId()));

        // Validate that home and away teams are different
        if (input.homeTeamId().equals(input.awayTeamId())) {
            throw new IllegalArgumentException("Home team and away team cannot be the same");
        }

        Match match = Match.builder()
                .matchDate(input.matchDate())
                .venue(input.venue())
                .status(Match.MatchStatus.SCHEDULED)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();
        
        return matchRepository.save(match);
    }

    @MutationMapping
    public Match updateMatchScore(@Argument("input") @Valid UpdateMatchScoreInput input) {
        Match match = matchRepository.findById(input.matchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + input.matchId()));

        match.setHomeTeamScore(input.homeTeamScore());
        match.setAwayTeamScore(input.awayTeamScore());
        match.setStatus(input.status());
        
        return matchRepository.save(match);
    }

    @MutationMapping
    public Match startMatch(@Argument Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + matchId));
        
        match.setStatus(Match.MatchStatus.LIVE);
        return matchRepository.save(match);
    }

    @MutationMapping
    public Match endMatch(@Argument Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + matchId));
        
        match.setStatus(Match.MatchStatus.COMPLETED);
        return matchRepository.save(match);
    }

    // ==================== STATS MUTATIONS ====================

    @MutationMapping
    public Stats recordStats(@Argument("input") @Valid RecordStatsInput input) {
        Player player = playerRepository.findById(input.playerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + input.playerId()));
        Match match = matchRepository.findById(input.matchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + input.matchId()));

        // Check if stats already exist for this player in this match
        Stats existingStats = statsRepository.findByPlayerIdAndMatchId(input.playerId(), input.matchId());
        if (existingStats != null) {
            throw new IllegalArgumentException("Stats already recorded for player " + input.playerId() + " in match " + input.matchId());
        }

        // Validate shooting statistics
        if (input.fieldGoalsMade() > input.fieldGoalsAttempted()) {
            throw new IllegalArgumentException("Field goals made cannot exceed field goals attempted");
        }
        if (input.threePointersMade() > input.threePointersAttempted()) {
            throw new IllegalArgumentException("Three pointers made cannot exceed three pointers attempted");
        }
        if (input.freeThrowsMade() > input.freeThrowsAttempted()) {
            throw new IllegalArgumentException("Free throws made cannot exceed free throws attempted");
        }

        Stats stats = Stats.builder()
                .player(player)
                .match(match)
                .points(input.points())
                .assists(input.assists())
                .rebounds(input.rebounds())
                .steals(input.steals())
                .blocks(input.blocks())
                .fieldGoalsMade(input.fieldGoalsMade())
                .fieldGoalsAttempted(input.fieldGoalsAttempted())
                .threePointersMade(input.threePointersMade())
                .threePointersAttempted(input.threePointersAttempted())
                .freeThrowsMade(input.freeThrowsMade())
                .freeThrowsAttempted(input.freeThrowsAttempted())
                .minutesPlayed(input.minutesPlayed())
                .build();
        
        return statsRepository.save(stats);
    }

    @MutationMapping
    public Stats updateStats(@Argument("input") @Valid RecordStatsInput input) {
        // Find existing stats record
        Stats stats = statsRepository.findByPlayerIdAndMatchId(input.playerId(), input.matchId());
        if (stats == null) {
            throw new IllegalArgumentException("Stats not found for player " + input.playerId() + " in match " + input.matchId());
        }
        
        // Update all fields since they are non-null in schema
        stats.setPoints(input.points());
        stats.setAssists(input.assists());
        stats.setRebounds(input.rebounds());
        stats.setSteals(input.steals());
        stats.setBlocks(input.blocks());
        stats.setFieldGoalsMade(input.fieldGoalsMade());
        stats.setFieldGoalsAttempted(input.fieldGoalsAttempted());
        stats.setThreePointersMade(input.threePointersMade());
        stats.setThreePointersAttempted(input.threePointersAttempted());
        stats.setFreeThrowsMade(input.freeThrowsMade());
        stats.setFreeThrowsAttempted(input.freeThrowsAttempted());
        Optional.ofNullable(input.minutesPlayed()).ifPresent(stats::setMinutesPlayed);
        
        return statsRepository.save(stats);
    }



    // ==================== INPUT RECORD CLASSES ====================

    public record CreateTeamInput(
            @NotBlank(message = "Team name is required") String name,
            @NotBlank(message = "City is required") String city,
            @Min(value = 1800, message = "Founded year must be after 1800") @Max(value = 2030, message = "Founded year cannot be in the future") Integer foundedYear,
            String coachName,
            String homeStadium
    ) {}

    public record UpdateTeamInput(
            @NotNull(message = "Team ID is required") Long id,
            String name,
            String city,
            @Min(value = 1800, message = "Founded year must be after 1800") @Max(value = 2030, message = "Founded year cannot be in the future") Integer foundedYear,
            String coachName,
            String homeStadium
    ) {}

    public record CreatePlayerInput(
            @NotBlank(message = "First name is required") String firstName,
            @NotBlank(message = "Last name is required") String lastName,
            @NotNull(message = "Jersey number is required") @Min(value = 0, message = "Jersey number must be positive") @Max(value = 99, message = "Jersey number must be less than 100") Integer jerseyNumber,
            @NotNull(message = "Position is required") Position position,
            @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,
            @Min(value = 100, message = "Height must be at least 100cm") @Max(value = 250, message = "Height cannot exceed 250cm") Integer height,
            @Min(value = 40, message = "Weight must be at least 40kg") @Max(value = 200, message = "Weight cannot exceed 200kg") Integer weight,
            @NotNull(message = "Team ID is required") Long teamId
    ) {}

    public record UpdatePlayerInput(
            @NotNull(message = "Player ID is required") Long id,
            String firstName,
            String lastName,
            @Min(value = 0, message = "Jersey number must be positive") @Max(value = 99, message = "Jersey number must be less than 100") Integer jerseyNumber,
            Position position,
            @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,
            @Min(value = 100, message = "Height must be at least 100cm") @Max(value = 250, message = "Height cannot exceed 250cm") Integer height,
            @Min(value = 40, message = "Weight must be at least 40kg") @Max(value = 200, message = "Weight cannot exceed 200kg") Integer weight,
            Long teamId
    ) {}

    public record CreateMatchInput(
            @NotNull(message = "Match date is required") @Future(message = "Match date must be in the future") LocalDateTime matchDate,
            @NotBlank(message = "Venue is required") String venue,
            @NotNull(message = "Home team ID is required") Long homeTeamId,
            @NotNull(message = "Away team ID is required") Long awayTeamId
    ) {}

    public record UpdateMatchScoreInput(
            @NotNull(message = "Match ID is required") Long matchId,
            @NotNull(message = "Home team score is required") @Min(value = 0, message = "Score cannot be negative") Integer homeTeamScore,
            @NotNull(message = "Away team score is required") @Min(value = 0, message = "Score cannot be negative") Integer awayTeamScore,
            @NotNull(message = "Match status is required") Match.MatchStatus status
    ) {}

    public record RecordStatsInput(
            @NotNull(message = "Player ID is required") Long playerId,
            @NotNull(message = "Match ID is required") Long matchId,
            @NotNull(message = "Points is required") @Min(value = 0, message = "Points cannot be negative") Integer points,
            @NotNull(message = "Assists is required") @Min(value = 0, message = "Assists cannot be negative") Integer assists,
            @NotNull(message = "Rebounds is required") @Min(value = 0, message = "Rebounds cannot be negative") Integer rebounds,
            @NotNull(message = "Steals is required") @Min(value = 0, message = "Steals cannot be negative") Integer steals,
            @NotNull(message = "Blocks is required") @Min(value = 0, message = "Blocks cannot be negative") Integer blocks,
            @NotNull(message = "Field goals made is required") @Min(value = 0, message = "Field goals made cannot be negative") Integer fieldGoalsMade,
            @NotNull(message = "Field goals attempted is required") @Min(value = 0, message = "Field goals attempted cannot be negative") Integer fieldGoalsAttempted,
            @NotNull(message = "Three pointers made is required") @Min(value = 0, message = "Three pointers made cannot be negative") Integer threePointersMade,
            @NotNull(message = "Three pointers attempted is required") @Min(value = 0, message = "Three pointers attempted cannot be negative") Integer threePointersAttempted,
            @NotNull(message = "Free throws made is required") @Min(value = 0, message = "Free throws made cannot be negative") Integer freeThrowsMade,
            @NotNull(message = "Free throws attempted is required") @Min(value = 0, message = "Free throws attempted cannot be negative") Integer freeThrowsAttempted,
            @Min(value = 0, message = "Minutes played cannot be negative") @Max(value = 48, message = "Minutes played cannot exceed 48") Integer minutesPlayed
    ) {}
}