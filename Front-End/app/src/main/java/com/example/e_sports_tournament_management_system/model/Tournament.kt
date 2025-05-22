package com.example.e_sports_tournament_management_system.model

data class Tournament(
    val id: Long? = null,
    val name: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val game: Game? = null,
    val matches: List<Match>? = emptyList()
)
