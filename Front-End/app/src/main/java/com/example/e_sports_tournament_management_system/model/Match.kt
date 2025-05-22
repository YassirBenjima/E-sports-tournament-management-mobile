package com.example.e_sports_tournament_management_system.model

import com.example.e_sports_tournament_management_system.model.Team
import com.example.e_sports_tournament_management_system.model.MatchResult

data class Match(
    val id: Long? = null,
    val team1: Team,
    val team2: Team,
    val result: MatchResult?,
    val tournament: Tournament? = null

)
