package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.model.MatchResult
import com.example.e_sports_tournament_management_system.model.Team
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditMatchActivity : AppCompatActivity() {

    private var matchId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_match)

        val team1Field = findViewById<EditText>(R.id.editTextMatchTeam1)
        val team2Field = findViewById<EditText>(R.id.editTextMatchTeam2)
        val scoreAField = findViewById<EditText>(R.id.editTextScoreA)
        val scoreBField = findViewById<EditText>(R.id.editTextScoreB)
        val winnerField = findViewById<EditText>(R.id.editTextWinnerName)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        // Récupération des données transmises via Intent
        matchId = intent.getLongExtra("MATCH_ID", -1)
        val team1Name = intent.getStringExtra("TEAM1_NAME") ?: ""
        val team2Name = intent.getStringExtra("TEAM2_NAME") ?: ""
        val teamAScore = intent.getIntExtra("TEAM1_SCORE", 0)
        val teamBScore = intent.getIntExtra("TEAM2_SCORE", 0)
        val winnerName = intent.getStringExtra("WINNER_NAME") ?: ""

        // Pré-remplir les champs
        team1Field.setText(team1Name)
        team2Field.setText(team2Name)
        scoreAField.setText(teamAScore.toString())
        scoreBField.setText(teamBScore.toString())
        winnerField.setText(winnerName)

        btnSubmit.text = "Mettre à jour"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnSubmit.setOnClickListener {
            val team1 = team1Field.text.toString().trim()
            val team2 = team2Field.text.toString().trim()
            val scoreA = scoreAField.text.toString().toIntOrNull()
            val scoreB = scoreBField.text.toString().toIntOrNull()
            val winner = winnerField.text.toString().trim()

            if (team1.isEmpty() || team2.isEmpty() || scoreA == null || scoreB == null || winner.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedMatch = Match(
                id = matchId,
                team1 = Team(name = team1, country = "", logoUrl = null),
                team2 = Team(name = team2, country = "", logoUrl = null),
                result = MatchResult(
                    teamAScore = scoreA,
                    teamBScore = scoreB,
                    winner = Team(name = winner, country = "", logoUrl = null)
                )
            )

            apiService.updateMatch(matchId, updatedMatch).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditMatchActivity, "Match mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditMatchActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    Toast.makeText(this@EditMatchActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnBack.setOnClickListener { finish() }
    }
}
