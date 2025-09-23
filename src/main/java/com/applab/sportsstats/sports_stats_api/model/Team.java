package com.applab.sportsstats.sports_stats_api.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(name = "founded_year")
    private String foundedYear;

    @Column(name = "home_stadium")
    private String home_stadium;

    @Column(name = "championships_won")
    private Integer championshipsWon;

    @Column(name = "coach_name")
    private String coachName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Player> players;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Match> matches;

    @Column(name= "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    protected void onCreate() {
         createdAt = LocalDate.now();
    }
}
