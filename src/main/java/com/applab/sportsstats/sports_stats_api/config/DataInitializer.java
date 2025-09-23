package com.applab.sportsstats.sports_stats_api.config;

import com.applab.sportsstats.sports_stats_api.entity.*;
import com.applab.sportsstats.sports_stats_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final StatsRepository statsRepository;

    @Override
    public void run(String... args) throws Exception {
        if (teamRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeData();
            log.info("Sample data initialized successfully!");
        } else {
            log.info("Data already exists, skipping initialization.");
        }
    }

    private void initializeData() {
        // Create Teams
        Team lakers = createTeam("Los Angeles Lakers", "Los Angeles", 1947, "Darvin Ham", "Crypto.com Arena");
        Team warriors = createTeam("Golden State Warriors", "San Francisco", 1946, "Steve Kerr", "Chase Center");
        Team celtics = createTeam("Boston Celtics", "Boston", 1946, "Joe Mazzulla", "TD Garden");
        Team heat = createTeam("Miami Heat", "Miami", 1988, "Erik Spoelstra", "FTX Arena");

        // Create Players for Lakers
        Player lebron = createPlayer("LeBron", "James", 6, Position.SMALL_FORWARD, 
                                   LocalDate.of(1984, 12, 30), 206, 113, lakers);
        Player davis = createPlayer("Anthony", "Davis", 3, Position.POWER_FORWARD, 
                                  LocalDate.of(1993, 3, 11), 208, 115, lakers);
        Player russell = createPlayer("D'Angelo", "Russell", 1, Position.POINT_GUARD, 
                                    LocalDate.of(1996, 2, 23), 196, 88, lakers);

        // Create Players for Warriors
        Player curry = createPlayer("Stephen", "Curry", 30, Position.POINT_GUARD, 
                                  LocalDate.of(1988, 3, 14), 191, 84, warriors);
        Player thompson = createPlayer("Klay", "Thompson", 11, Position.SHOOTING_GUARD, 
                                     LocalDate.of(1990, 2, 8), 198, 98, warriors);
        Player green = createPlayer("Draymond", "Green", 23, Position.POWER_FORWARD, 
                                  LocalDate.of(1990, 3, 4), 201, 104, warriors);

        // Create Players for Celtics
        Player tatum = createPlayer("Jayson", "Tatum", 0, Position.SMALL_FORWARD, 
                                  LocalDate.of(1998, 3, 3), 203, 95, celtics);
        Player brown = createPlayer("Jaylen", "Brown", 7, Position.SHOOTING_GUARD, 
                                  LocalDate.of(1996, 10, 24), 201, 101, celtics);

        // Create Players for Heat
        Player butler = createPlayer("Jimmy", "Butler", 22, Position.SMALL_FORWARD, 
                                   LocalDate.of(1989, 9, 14), 201, 104, heat);
        Player adebayo = createPlayer("Bam", "Adebayo", 13, Position.CENTER, 
                                    LocalDate.of(1997, 7, 18), 206, 116, heat);

        // Create Matches
        Match match1 = createMatch(LocalDateTime.of(2024, 1, 15, 20, 0), 
                                 "Crypto.com Arena", Match.MatchStatus.COMPLETED, 
                                 lakers, warriors, 112, 108);

        Match match2 = createMatch(LocalDateTime.of(2024, 1, 20, 19, 30), 
                                 "TD Garden", Match.MatchStatus.COMPLETED, 
                                 celtics, heat, 118, 102);

        Match match3 = createMatch(LocalDateTime.of(2024, 1, 25, 21, 0), 
                                 "Chase Center", Match.MatchStatus.LIVE, 
                                 warriors, celtics, 95, 88);

        // Create Stats for Match 1 (Lakers vs Warriors)
        createStats(lebron, match1, 28, 8, 11, 2, 1, 10, 18, 2, 6, 6, 8, 38);
        createStats(davis, match1, 22, 3, 14, 1, 3, 8, 15, 0, 2, 6, 8, 42);
        createStats(russell, match1, 18, 12, 4, 3, 0, 6, 14, 3, 8, 3, 4, 35);
        
        createStats(curry, match1, 31, 6, 5, 2, 0, 11, 22, 7, 15, 2, 2, 40);
        createStats(thompson, match1, 24, 2, 6, 1, 1, 9, 18, 6, 12, 0, 0, 36);
        createStats(green, match1, 8, 9, 8, 2, 2, 3, 7, 0, 3, 2, 4, 35);

        // Create Stats for Match 2 (Celtics vs Heat)
        createStats(tatum, match2, 35, 4, 9, 1, 0, 12, 20, 5, 10, 6, 7, 41);
        createStats(brown, match2, 26, 3, 7, 2, 1, 10, 16, 4, 8, 2, 3, 38);
        
        createStats(butler, match2, 19, 6, 5, 3, 1, 7, 15, 1, 4, 4, 6, 39);
        createStats(adebayo, match2, 15, 2, 12, 0, 2, 6, 10, 0, 0, 3, 5, 35);

        log.info("Created {} teams, {} players, {} matches, {} stats entries", 
                teamRepository.count(), playerRepository.count(), 
                matchRepository.count(), statsRepository.count());
    }

    private Team createTeam(String name, String city, Integer foundedYear, 
                           String coachName, String homeStadium) {
        Team team = Team.builder()
                .name(name)
                .city(city)
                .foundedYear(foundedYear)
                .coachName(coachName)
                .homeStadium(homeStadium)
                .build();
        return teamRepository.save(team);
    }

    private Player createPlayer(String firstName, String lastName, Integer jerseyNumber, 
                              Position position, LocalDate dateOfBirth, 
                              Integer height, Integer weight, Team team) {
        Player player = Player.builder()
                .firstName(firstName)
                .lastName(lastName)
                .jerseyNumber(jerseyNumber)
                .position(position)
                .dateOfBirth(dateOfBirth)
                .height(height)
                .weight(weight)
                .team(team)
                .build();
        return playerRepository.save(player);
    }

    private Match createMatch(LocalDateTime matchDate, String venue, Match.MatchStatus status,
                            Team homeTeam, Team awayTeam, Integer homeScore, Integer awayScore) {
        Match match = Match.builder()
                .matchDate(matchDate)
                .venue(venue)
                .status(status)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeTeamScore(homeScore)
                .awayTeamScore(awayScore)
                .teams(Arrays.asList(homeTeam, awayTeam))
                .build();
        return matchRepository.save(match);
    }

    private Stats createStats(Player player, Match match, Integer points, Integer assists, 
                            Integer rebounds, Integer steals, Integer blocks, 
                            Integer fgm, Integer fga, Integer tpm, Integer tpa, 
                            Integer ftm, Integer fta, Integer minutes) {
        Stats stats = Stats.builder()
                .player(player)
                .match(match)
                .points(points)
                .assists(assists)
                .rebounds(rebounds)
                .steals(steals)
                .blocks(blocks)
                .fieldGoalsMade(fgm)
                .fieldGoalsAttempted(fga)
                .threePointersMade(tpm)
                .threePointersAttempted(tpa)
                .freeThrowsMade(ftm)
                .freeThrowsAttempted(fta)
                .minutesPlayed(minutes)
                .build();
        return statsRepository.save(stats);
    }
}
