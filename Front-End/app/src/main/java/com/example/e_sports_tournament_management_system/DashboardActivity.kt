package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val options = listOf("Tournois", "Équipes", "Matchs", "Profil")

        val username = intent.getStringExtra("USERNAME") ?: "Utilisateur"

        val textWelcome = findViewById<TextView>(R.id.textWelcome)
        textWelcome.text = "Bienvenue, $username"

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Revenir à la page de connexion
            val intent = Intent(this@DashboardActivity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
        }

        val btnUtilisateur = findViewById<Button>(R.id.btnUtilisateur)
        btnUtilisateur.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
        val btnGames = findViewById<Button>(R.id.btnGames)
        btnGames.setOnClickListener {
            val intent = Intent(this@DashboardActivity, GamesActivity::class.java)
            startActivity(intent)
        }
        val btnPlayers = findViewById<Button>(R.id.btnPlayers)
        btnPlayers.setOnClickListener {
            val intent = Intent(this@DashboardActivity, PlayersActivity::class.java)
            startActivity(intent)
        }
        val btnTeams = findViewById<Button>(R.id.btnTeams)
        btnTeams.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TeamsActivity::class.java)
            startActivity(intent)
        }
        val btnMatches = findViewById<Button>(R.id.btnMatches)
        btnMatches.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MatchesActivity::class.java)
            startActivity(intent)
        }

        val btnTournaments = findViewById<Button>(R.id.btnTournaments)
        btnTournaments.setOnClickListener {
            val intent = Intent(this, TournamentsActivity::class.java)
            startActivity(intent)
        }






    }
}
