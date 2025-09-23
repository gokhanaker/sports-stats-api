package com.applab.sportsstats.sports_stats_api.repository;

import com.applab.sportsstats.sports_stats_api.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    
    List<Match> findByStatus(Match.MatchStatus status);
    
    List<Match> findByMatchDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT m FROM Match m WHERE m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId")
    List<Match> findByTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT m FROM Match m WHERE (m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId) AND m.status = :status")
    List<Match> findByTeamIdAndStatus(@Param("teamId") Long teamId, @Param("status") Match.MatchStatus status);
    
    @Query("SELECT m FROM Match m WHERE m.homeTeam.id = :homeTeamId AND m.awayTeam.id = :awayTeamId")
    List<Match> findByHomeTeamAndAwayTeam(@Param("homeTeamId") Long homeTeamId, 
                                         @Param("awayTeamId") Long awayTeamId);
    
    List<Match> findByVenueContainingIgnoreCase(String venue);
    
    @Query("SELECT m FROM Match m WHERE m.status = 'LIVE' ORDER BY m.matchDate DESC")
    List<Match> findLiveMatches();
    
    @Query("SELECT m FROM Match m WHERE m.matchDate > :currentDate ORDER BY m.matchDate ASC")
    List<Match> findUpcomingMatches(@Param("currentDate") LocalDateTime currentDate);
}
