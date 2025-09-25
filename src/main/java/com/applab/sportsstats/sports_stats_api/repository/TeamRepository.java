package com.applab.sportsstats.sports_stats_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    // Filtering methods
    @Query("SELECT t FROM Team t WHERE " +
           "(:city IS NULL OR LOWER(t.city) = LOWER(:city)) AND " +
           "(:minFoundedYear IS NULL OR t.foundedYear >= :minFoundedYear) AND " +
           "(:maxFoundedYear IS NULL OR t.foundedYear <= :maxFoundedYear) AND " +
           "(:nameContains IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :nameContains, '%'))) AND " +
           "(:coachName IS NULL OR LOWER(t.coachName) LIKE LOWER(CONCAT('%', :coachName, '%')))")
    Page<Team> findWithFilters(
        @Param("city") String city,
        @Param("minFoundedYear") Integer minFoundedYear,
        @Param("maxFoundedYear") Integer maxFoundedYear,
        @Param("nameContains") String nameContains,
        @Param("coachName") String coachName,
        Pageable pageable
    );
}
