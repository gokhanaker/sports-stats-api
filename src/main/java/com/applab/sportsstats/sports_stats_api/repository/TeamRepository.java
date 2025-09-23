package com.applab.sportsstats.sports_stats_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.applab.sportsstats.sports_stats_api.entity.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    Optional<Team> findByName(String name);
    
    List<Team> findByCity(String city);
    
    List<Team> findByFoundedYearGreaterThan(Integer year);
    
    @Query("SELECT t FROM Team t WHERE t.coachName = :coachName")
    List<Team> findByCoachName(@Param("coachName") String coachName);
    
    @Query("SELECT t FROM Team t JOIN t.players p WHERE p.id = :playerId")
    Optional<Team> findByPlayerId(@Param("playerId") Long playerId);
}
