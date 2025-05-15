//package com.esport.E_sports_tournament_management_system.controller;
//
//import com.esport.E_sports_tournament_management_system.model.Match;
//import com.esport.E_sports_tournament_management_system.service.MatchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/matches")
//public class MatchController {
//
//    @Autowired
//    private MatchService matchService;
//
//    @GetMapping
//    public List<Match> getAllMatches() {
//        return matchService.getAllMatches();
//    }
//
//    @PostMapping
//    public Match addMatch(@RequestBody Match match) {
//        return matchService.addMatch(match);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody Match updatedMatch) {
//        return matchService.getMatchById(id)
//                .map(match -> {
//                    match.setTeam1(updatedMatch.getTeam1());
//                    match.setTeam2(updatedMatch.getTeam2());
//                    match.setResult(updatedMatch.getResult());
//                    return ResponseEntity.ok(matchService.updateMatch(match));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
//        if (matchService.getMatchById(id).isPresent()) {
//            matchService.deleteMatch(id);
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
