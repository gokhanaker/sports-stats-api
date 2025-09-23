package com.applab.sportsstats.sports_stats_api;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.enums.Position;
import com.applab.sportsstats.sports_stats_api.entity.Team;
import com.applab.sportsstats.sports_stats_api.repository.MatchRepository;
import com.applab.sportsstats.sports_stats_api.repository.PlayerRepository;
import com.applab.sportsstats.sports_stats_api.repository.StatsRepository;
import com.applab.sportsstats.sports_stats_api.repository.TeamRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private StatsRepository statsRepository;

    @Test
    void testTeamRepository() {
        // Test will run with the DataInitializer data
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).isNotEmpty();
        
        Optional<Team> lakers = teamRepository.findByName("Los Angeles Lakers");
        assertThat(lakers).isPresent();
        assertThat(lakers.get().getCity()).isEqualTo("Los Angeles");
    }

    @Test
    void testPlayerRepository() {
        List<Player> players = playerRepository.findAll();
        assertThat(players).isNotEmpty();
        
        List<Player> pointGuards = playerRepository.findByPosition(Position.POINT_GUARD);
        assertThat(pointGuards).isNotEmpty();
    }
}