package com.applab.sportsstats.sports_stats_api;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.entity.Team;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.repository.PlayerRepository;
import com.applab.sportsstats.sports_stats_api.repository.TeamRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        // Create test data for each test
        Team testTeam = Team.builder()
                .name("Test Lakers")
                .city("Los Angeles")
                .foundedYear(1947)
                .coachName("Test Coach")
                .homeStadium("Test Arena")
                .createdAt(LocalDate.now())
                .build();
        teamRepository.save(testTeam);

        Player testPlayer = Player.builder()
                .firstName("Test")
                .lastName("Player")
                .jerseyNumber(23)
                .position(Position.POINT_GUARD)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .height(190)
                .weight(85)
                .team(testTeam)
                .createdAt(LocalDate.now())
                .build();
        playerRepository.save(testPlayer);
    }

    @Test
    void testTeamRepository() {
        // Test with our test data
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).isNotEmpty();
        
        Optional<Team> testLakers = teamRepository.findByName("Test Lakers");
        assertThat(testLakers).isPresent();
        assertThat(testLakers.get().getCity()).isEqualTo("Los Angeles");
    }

    @Test
    void testPlayerRepository() {
        List<Player> players = playerRepository.findAll();
        assertThat(players).isNotEmpty();
        
        List<Player> pointGuards = playerRepository.findByPosition(Position.POINT_GUARD);
        assertThat(pointGuards).isNotEmpty();
        assertThat(pointGuards.get(0).getFirstName()).isEqualTo("Test");
    }
}