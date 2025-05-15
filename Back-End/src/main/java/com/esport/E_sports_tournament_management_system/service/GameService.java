package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.Game;
import com.esport.E_sports_tournament_management_system.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // Mettre à jour un jeu
    public Game updateGame(Long id, Game updatedGame) {
        return gameRepository.findById(id)
                .map(game -> {
                    game.setName(updatedGame.getName());
                    game.setPlatform(updatedGame.getPlatform());
                    game.setImageUrl(updatedGame.getImageUrl());
                    return gameRepository.save(game);
                })
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public String uploadGameImage(Long gameId, MultipartFile file) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Game not found with id: " + gameId);
        }

        if (file.isEmpty()) {
            throw new RuntimeException("No file selected for upload");
        }

        try {
            File uploadDir = new File("uploads/game_images/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadDir, fileName);
            file.transferTo(destinationFile);

            Game game = optionalGame.get();
            game.setImageUrl("/uploads/game_images/" + fileName);
            gameRepository.save(game);

            return game.getImageUrl();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}
