package com.applab.sportsstats.sports_stats_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.applab.sportsstats.sports_stats_api.entity.Player;
import com.applab.sportsstats.sports_stats_api.enums.Position;

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
    
    // Paginated versions
    Page<Player> findByTeamId(Long teamId, Pageable pageable);
    
    Page<Player> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);
    
    // Filtering methods
    @Query("SELECT p FROM Player p WHERE " +
           "(:teamId IS NULL OR p.team.id = :teamId) AND " +
           "(:position IS NULL OR p.position = :position) AND " +
           "(:minJerseyNumber IS NULL OR p.jerseyNumber >= :minJerseyNumber) AND " +
           "(:maxJerseyNumber IS NULL OR p.jerseyNumber <= :maxJerseyNumber)")
    Page<Player> findWithBasicFilters(
        @Param("teamId") Long teamId,
        @Param("position") Position position,
        @Param("minJerseyNumber") Integer minJerseyNumber,
        @Param("maxJerseyNumber") Integer maxJerseyNumber,
        Pageable pageable
    );
    
    // Advanced filtering with stats aggregation
    @Query("SELECT DISTINCT p FROM Player p " +
           "LEFT JOIN p.stats s " +
           "WHERE (:teamId IS NULL OR p.team.id = :teamId) " +
           "AND (:position IS NULL OR p.position = :position) " +
           "AND (:minJerseyNumber IS NULL OR p.jerseyNumber >= :minJerseyNumber) " +
           "AND (:maxJerseyNumber IS NULL OR p.jerseyNumber <= :maxJerseyNumber) " +
           "GROUP BY p.id " +
           "HAVING (:minPoints IS NULL OR AVG(s.points) >= :minPoints) " +
           "AND (:maxPoints IS NULL OR AVG(s.points) <= :maxPoints)")
    Page<Player> findWithAdvancedFilters(
        @Param("teamId") Long teamId,
        @Param("position") Position position,
        @Param("minJerseyNumber") Integer minJerseyNumber,
        @Param("maxJerseyNumber") Integer maxJerseyNumber,
        @Param("minPoints") Integer minPoints,
        @Param("maxPoints") Integer maxPoints,
        Pageable pageable
    );
}