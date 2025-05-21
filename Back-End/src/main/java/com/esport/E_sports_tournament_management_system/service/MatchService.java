package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.Match;
import com.esport.E_sports_tournament_management_system.model.Team;
import com.esport.E_sports_tournament_management_system.repository.MatchRepository;
import com.esport.E_sports_tournament_management_system.repository.MatchResultRepository;
import com.esport.E_sports_tournament_management_system.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchResultRepository matchResultRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Autowired
    private MatchResultService matchResultService;

    public Match addMatch(Match match) {
        Team team1 = teamRepository.findByName(match.getTeam1().getName())
                .orElseThrow(() -> new RuntimeException("Team 1 not found"));
        Team team2 = teamRepository.findByName(match.getTeam2().getName())
                .orElseThrow(() -> new RuntimeException("Team 2 not found"));
        Team winner = teamRepository.findByName(match.getResult().getWinner().getName())
                .orElseThrow(() -> new RuntimeException("Winner team not found"));

        match.setTeam1(team1);
        match.setTeam2(team2);
        match.getResult().setWinner(winner);

        // Save MatchResult first if needed
        matchResultRepository.save(match.getResult());

        return matchRepository.save(match);
    }



    public Match updateMatch(Long id, Match updatedMatch) {
        return matchRepository.findById(id).map(existing -> {
            // 🔁 Récupérer les équipes existantes par nom
            Team team1 = teamRepository.findByName(updatedMatch.getTeam1().getName())
                    .orElseThrow(() -> new RuntimeException("Team 1 not found"));
            Team team2 = teamRepository.findByName(updatedMatch.getTeam2().getName())
                    .orElseThrow(() -> new RuntimeException("Team 2 not found"));
            Team winner = teamRepository.findByName(updatedMatch.getResult().getWinner().getName())
                    .orElseThrow(() -> new RuntimeException("Winner team not found"));

            existing.setTeam1(team1);
            existing.setTeam2(team2);
            existing.getResult().setTeamAScore(updatedMatch.getResult().getTeamAScore());
            existing.getResult().setTeamBScore(updatedMatch.getResult().getTeamBScore());
            existing.getResult().setWinner(winner);

            matchResultRepository.save(existing.getResult());
            return matchRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Match not found"));
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }


}
