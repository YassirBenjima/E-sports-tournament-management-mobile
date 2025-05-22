package com.esport.E_sports_tournament_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = true)
    private Tournament tournament;

    @ManyToOne
    private Team team1;

    @ManyToOne
    private Team team2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true ) // allows null in DB
    private Date matchDate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_result_id", referencedColumnName = "id" , nullable = true )
    private MatchResult result;


    public Match() {}

    public Match(Tournament tournament, Team team1, Team team2, MatchResult result) {
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public MatchResult getResult() {
        return result;
    }

    public Date getMatchDate() {
        return matchDate;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setTeam1(Team teamA) {
        this.team1 = teamA;
    }

    public void setTeam2(Team teamB) {
        this.team2 = teamB;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

}
