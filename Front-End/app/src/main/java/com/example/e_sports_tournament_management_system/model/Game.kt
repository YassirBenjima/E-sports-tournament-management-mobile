package com.example.e_sports_tournament_management_system.model

data class Game(
    val id: Long? = null,
    val name: String,
    val platform: String,
    val imageUrl: String? = null  // Champ ajouté pour l'URL de l'image
)
