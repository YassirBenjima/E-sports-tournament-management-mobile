package com.example.e_sports_tournament_management_system.model

data class Player(
    val id: Long? = null,
    val username: String,
    val age: Int,
    val nationality: String,
    val imageUrl: String? = null
)
