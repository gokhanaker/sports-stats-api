package com.applab.sportsstats.sports_stats_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.entity.Position;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    List<Player> findByTeamId(Long teamId);
    
    List<Player> findByPosition(Position position);
    
    Optional<Player> findByJerseyNumber(Integer jerseyNumber);
    
    List<Player> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
    
    @Query("SELECT p FROM Player p WHERE p.team.id = :teamId AND p.position = :position")
    List<Player> findByTeamIdAndPosition(@Param("teamId") Long teamId, 
                                        @Param("position") Position position);
    
    @Query("SELECT p FROM Player p JOIN p.stats s WHERE s.points >= :minPoints")
    List<Player> findPlayersWithMinimumPoints(@Param("minPoints") Integer minPoints);
    
    @Query("SELECT p FROM Player p WHERE p.team.name = :teamName")
    List<Player> findByTeamName(@Param("teamName") String teamName);
}