package com.example.e_sports_tournament_management_system.model

data class Team(
    val id: Long? = null,
    val name: String,
    val country: String,
    val logoUrl: String? = null
)
