package com.applab.sportsstats.sports_stats_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.applab.sportsstats.sports_stats_api.entity.Stats;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    
    List<Stats> findByPlayerId(Long playerId);
    
    List<Stats> findByMatchId(Long matchId);
    
    @Query("SELECT s FROM Stats s WHERE s.player.id = :playerId AND s.match.id = :matchId")
    Stats findByPlayerIdAndMatchId(@Param("playerId") Long playerId, @Param("matchId") Long matchId);
    
    @Query("SELECT s FROM Stats s JOIN s.player p WHERE s.points >= :minPoints")
    List<Stats> findPlayersWithMinimumPoints(@Param("minPoints") Integer minPoints);
    
    @Query("SELECT s FROM Stats s WHERE s.player.id = :playerId ORDER BY s.points DESC")
    List<Stats> findByPlayerIdOrderByPointsDesc(@Param("playerId") Long playerId);
    
    @Query("SELECT s FROM Stats s WHERE s.points >= :minPoints ORDER BY s.points DESC")
    List<Stats> findTopScorers(@Param("minPoints") Integer minPoints);
    
    @Query("SELECT s FROM Stats s WHERE s.assists >= :minAssists ORDER BY s.assists DESC")
    List<Stats> findTopAssistProviders(@Param("minAssists") Integer minAssists);
    
    @Query("SELECT s FROM Stats s WHERE s.rebounds >= :minRebounds ORDER BY s.rebounds DESC")
    List<Stats> findTopRebounders(@Param("minRebounds") Integer minRebounds);
    
    @Query("SELECT s FROM Stats s JOIN s.player p WHERE p.team.id = :teamId")
    List<Stats> findByTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT s FROM Stats s WHERE s.match.id = :matchId AND s.player.team.id = :teamId")
    List<Stats> findByMatchIdAndTeamId(@Param("matchId") Long matchId, @Param("teamId") Long teamId);
    
    // Aggregate queries for leaderboards
    @Query("SELECT s.player.id, s.player.firstName, s.player.lastName, AVG(s.points) as avgPoints " +
           "FROM Stats s GROUP BY s.player.id, s.player.firstName, s.player.lastName " +
           "ORDER BY avgPoints DESC")
    List<Object[]> findAveragePointsLeaderboard();
    
    @Query("SELECT s.player.id, s.player.firstName, s.player.lastName, SUM(s.points) as totalPoints " +
           "FROM Stats s GROUP BY s.player.id, s.player.firstName, s.player.lastName " +
           "ORDER BY totalPoints DESC")
    List<Object[]> findTotalPointsLeaderboard();
}
