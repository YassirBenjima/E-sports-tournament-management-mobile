package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.Player;
import com.esport.E_sports_tournament_management_system.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private static final String UPLOAD_DIR = "uploads/player_images/";

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    // ✅ Méthode d'upload image centralisée dans le service
    public String uploadPlayerImage(Long playerId, MultipartFile file) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);

        if (optionalPlayer.isEmpty()) {
            throw new RuntimeException("Player not found with id: " + playerId);
        }

        if (file.isEmpty()) {
            throw new RuntimeException("No file selected for upload");
        }

        try {
            // Créer le dossier si inexistant
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Générer un nom unique pour le fichier
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Sauvegarder le fichier localement
            File destinationFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(destinationFile);

            // Mettre à jour le champ imageUrl du Player
            Player player = optionalPlayer.get();
            player.setImageUrl("/" + UPLOAD_DIR + fileName);
            playerRepository.save(player);

            return player.getImageUrl();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
