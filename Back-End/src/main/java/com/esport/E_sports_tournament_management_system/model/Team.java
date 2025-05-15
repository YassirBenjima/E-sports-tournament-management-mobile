package com.esport.E_sports_tournament_management_system.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String captain; // could be a player's username

    private String country;

    private String logoUrl;




    @ManyToMany
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players;

    // Constructors
    public Team() {}

    public Team(String name, String captain, List<Player> players) {
        this.name = name;
        this.captain = captain;
        this.players = players;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCaptain() {
        return captain;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getCountry() {
        return country;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }}
