package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.entity.Stats;
import com.applab.sportsstats.sports_stats_api.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlayerFieldResolver {

    private final StatsRepository statsRepository;

    @SchemaMapping(typeName = "Player", field = "fullName")
    public String fullName(Player player) {
        return player.getFirstName() + " " + player.getLastName();
    }

    @SchemaMapping(typeName = "Player", field = "age")
    public Integer age(Player player) {
        if (player.getDateOfBirth() == null) return null;
        return Period.between(player.getDateOfBirth(), LocalDate.now()).getYears();
    }

    private List<Stats> stats(Player player) {
        return statsRepository.findByPlayerId(player.getId());
    }

    @SchemaMapping(typeName = "Player", field = "averagePoints")
    public double averagePoints(Player player) {
        List<Stats> list = stats(player);
        if (list.isEmpty()) return 0.0;
        return list.stream().mapToInt(Stats::getPoints).average().orElse(0.0);
    }

    @SchemaMapping(typeName = "Player", field = "averageAssists")
    public double averageAssists(Player player) {
        List<Stats> list = stats(player);
        if (list.isEmpty()) return 0.0;
        return list.stream().mapToInt(Stats::getAssists).average().orElse(0.0);
    }

    @SchemaMapping(typeName = "Player", field = "averageRebounds")
    public double averageRebounds(Player player) {
        List<Stats> list = stats(player);
        if (list.isEmpty()) return 0.0;
        return list.stream().mapToInt(Stats::getRebounds).average().orElse(0.0);
    }

    @SchemaMapping(typeName = "Player", field = "totalGamesPlayed")
    public int totalGamesPlayed(Player player) {
        return stats(player).size();
    }
}
