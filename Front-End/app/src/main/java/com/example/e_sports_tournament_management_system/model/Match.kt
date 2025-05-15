package com.example.e_sports_tournament_management_system.model

data class Match(
    val id: Long? = null,
    val team1: String,
    val team2: String,
    val result: String
)
