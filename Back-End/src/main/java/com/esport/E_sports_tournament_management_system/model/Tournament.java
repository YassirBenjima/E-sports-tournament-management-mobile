package com.esport.E_sports_tournament_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonIgnoreProperties(ignoreUnknown = true)

    private LocalDate startDate;

    @JsonIgnoreProperties(ignoreUnknown = true)

    private LocalDate endDate;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Double prizePool;
    private String imageUrl;
    private String location;




    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Match> matches = new ArrayList<>();

    @ManyToOne
    private Team winner; // peut être null



    // Constructors
    public Tournament() {}

    public Tournament(String name, LocalDate startDate, LocalDate endDate, Double prizePool,String location) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prizePool = prizePool;
        this.location = location;

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

    public String getLocation() { return location; }

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

    public void setLocation(String location) { this.location = location; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}


