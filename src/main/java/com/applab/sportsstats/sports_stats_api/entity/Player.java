package com.applab.sportsstats.sports_stats_api.entity;

import java.time.LocalDate;
import java.util.List;

import com.applab.sportsstats.sports_stats_api.enums.Position;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name="jersey_number", nullable = false, unique = true)
    private Integer jerseyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "height")
    private Integer height; // in centimeters

    @Column(name = "weight")
    private Integer weight; // in kilograms

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @ToString.Exclude
    private Team team;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Stats> stats;

    @Column(name= "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDate.now();
    }
}
