package com.example.e_sports_tournament_management_system.model

data class User(
    val id: Long?=null,
    val username: String,
    val password: String,
    val fullName: String,
    val email: String
)
