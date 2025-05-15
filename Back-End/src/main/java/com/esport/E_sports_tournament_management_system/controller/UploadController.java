package com.esport.E_sports_tournament_management_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + filename);

            File parentDir = dest.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    System.err.println("Impossible de créer le dossier : " + parentDir.getAbsolutePath());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erreur serveur : impossible de créer le dossier d'upload");
                }
            }

            System.out.println("Chemin fichier destination : " + dest.getAbsolutePath());

            file.transferTo(dest);

            String baseUrl = "http://10.0.2.2:8080";
            String imageUrl = baseUrl + "/images/" + filename;
            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l’upload : " + e.getMessage());
        }
    }
}
