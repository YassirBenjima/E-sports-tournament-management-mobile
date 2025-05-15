package com.esport.E_sports_tournament_management_system.controller;

import com.esport.E_sports_tournament_management_system.model.Player;
import com.esport.E_sports_tournament_management_system.repository.PlayerRepository;
import com.esport.E_sports_tournament_management_system.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;


    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    player.setUsername(updatedPlayer.getUsername());
                    player.setAge(updatedPlayer.getAge());
                    player.setNationality(updatedPlayer.getNationality());
                    player.setImageUrl(updatedPlayer.getImageUrl());
                    return ResponseEntity.ok(playerService.updatePlayer(player));
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
