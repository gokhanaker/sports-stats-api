package com.applab.sportsstats.sports_stats_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.applab.sportsstats.sports_stats_api.entity.Match;

import java.time.OffsetDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    
    List<Match> findByStatus(Match.MatchStatus status);
    
    List<Match> findByMatchDateBetween(OffsetDateTime startDate, OffsetDateTime endDate);
    
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
    List<Match> findUpcomingMatches(@Param("currentDate") OffsetDateTime currentDate);
    
    // Paginated version
    @Query("SELECT m FROM Match m WHERE m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId")
    Page<Match> findByTeamId(@Param("teamId") Long teamId, Pageable pageable);
    
    // Filtering methods
    @Query("SELECT m FROM Match m WHERE " +
           "(:teamId IS NULL OR m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:dateFrom IS NULL OR m.matchDate >= :dateFrom) AND " +
           "(:dateTo IS NULL OR m.matchDate <= :dateTo) AND " +
           "(:venue IS NULL OR LOWER(m.venue) LIKE LOWER(CONCAT('%', :venue, '%'))) AND " +
           "(:hasScore IS NULL OR " +
           "    (:hasScore = true AND m.homeTeamScore IS NOT NULL AND m.awayTeamScore IS NOT NULL) OR " +
           "    (:hasScore = false AND (m.homeTeamScore IS NULL OR m.awayTeamScore IS NULL)))")
    Page<Match> findWithFilters(
        @Param("teamId") Long teamId,
        @Param("status") Match.MatchStatus status,
        @Param("dateFrom") OffsetDateTime dateFrom,
        @Param("dateTo") OffsetDateTime dateTo,
        @Param("venue") String venue,
        @Param("hasScore") Boolean hasScore,
        Pageable pageable
    );
}
