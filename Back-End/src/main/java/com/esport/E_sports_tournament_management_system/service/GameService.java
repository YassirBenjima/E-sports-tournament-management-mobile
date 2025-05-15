package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.Game;
import com.esport.E_sports_tournament_management_system.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Récupérer tous les jeux
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // Ajouter un jeu
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    // Supprimer un jeu par ID
    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    // Trouver un jeu par ID
    public Optional<Game> getGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    // Mettre à jour un jeu
    public Game updateGame(Long id, Game updatedGame) {
        return gameRepository.findById(id)
                .map(game -> {
                    game.setName(updatedGame.getName());
                    game.setPlatform(updatedGame.getPlatform());
                    return gameRepository.save(game);
                })
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }
}
