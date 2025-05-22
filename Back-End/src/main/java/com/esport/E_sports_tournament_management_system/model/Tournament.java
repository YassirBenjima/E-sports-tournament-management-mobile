package com.esport.E_sports_tournament_management_system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "game_id")


    private Game game;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double prizePool;
    private String imageUrl;



    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();

    @ManyToOne
    private Team winner; // peut être null



    // Constructors
    public Tournament() {}

    public Tournament(String name, LocalDate startDate, LocalDate endDate, Double prizePool) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prizePool = prizePool;

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getPrizePool() {
        return prizePool;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public Team getWinner() {
        return winner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPrizePool(Double prizePool) {
        this.prizePool = prizePool;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}


