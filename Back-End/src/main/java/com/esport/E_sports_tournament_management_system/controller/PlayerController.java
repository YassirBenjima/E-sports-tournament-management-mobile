package com.esport.E_sports_tournament_management_system.controller;

import com.esport.E_sports_tournament_management_system.model.Player;
import com.esport.E_sports_tournament_management_system.repository.PlayerRepository;
import com.esport.E_sports_tournament_management_system.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private static final String UPLOAD_DIR = "uploads/player_images/";

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

    // ✅ Upload image pour un player spécifique
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadPlayerImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);

        if (optionalPlayer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected");
        }

        try {
            // Créer le dossier si nécessaire
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Générer un nom de fichier unique
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Sauvegarder le fichier
            File destinationFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(destinationFile);

            // Mettre à jour le Player avec l'URL de l'image
            Player player = optionalPlayer.get();
            player.setImageUrl("/" + UPLOAD_DIR + fileName);
            playerRepository.save(player);

            return ResponseEntity.ok("Image uploaded successfully: " + player.getImageUrl());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }
}
