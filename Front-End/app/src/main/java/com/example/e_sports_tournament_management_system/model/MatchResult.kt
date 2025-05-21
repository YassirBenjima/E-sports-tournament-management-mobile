package com.example.e_sports_tournament_management_system.model

import com.example.e_sports_tournament_management_system.model.Team

data class MatchResult(
    val id: Long? = null,
    val teamAScore: Int,
    val teamBScore: Int,
    val winner: Team? = null
)
