package com.esport.E_sports_tournament_management_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "match_results")
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private int teamAScore;

    @Column(nullable = true)
    private int teamBScore;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = true)
    private Team winner;

    public MatchResult() {}

    public MatchResult(int teamAScore, int teamBScore, Team winner) {
        this.teamAScore = teamAScore;
        this.teamBScore = teamBScore;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public int getTeamAScore() {
        return teamAScore;
    }

    public int getTeamBScore() {
        return teamBScore;
    }

    public Team getWinner() {
        return winner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeamAScore(int teamAScore) {
        this.teamAScore = teamAScore;
    }

    public void setTeamBScore(int teamBScore) {
        this.teamBScore = teamBScore;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}
