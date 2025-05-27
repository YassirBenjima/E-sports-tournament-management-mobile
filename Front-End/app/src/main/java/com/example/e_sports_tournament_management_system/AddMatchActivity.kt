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
import android.util.Log
import com.example.e_sports_tournament_management_system.model.Tournament


class AddMatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CrashHandler", "💥 Exception : ${throwable.message}", throwable)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)

        Toast.makeText(this, "🟢 AddMatchActivity ouverte", Toast.LENGTH_SHORT).show()
        Log.d("AddMatchActivity", "🟢 onCreate exécuté")

        val team1Field = findViewById<EditText>(R.id.editTextMatchTeam1)
        val team2Field = findViewById<EditText>(R.id.editTextMatchTeam2)
        val scoreAField = findViewById<EditText>(R.id.editTextScoreA)
        val scoreBField = findViewById<EditText>(R.id.editTextScoreB)
        val winnerField = findViewById<EditText>(R.id.editTextWinnerName)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }
        Log.d("AddMatchActivity", "→ Bouton 'Ajouter' cliqué")
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

            val newMatch = Match(
                team1 = Team(name = team1, country = "", logoUrl = null),
                team2 = Team(name = team2, country = "", logoUrl = null),
                result = MatchResult(
                    teamAScore = scoreA,
                    teamBScore = scoreB,
                    winner = Team(name = winner, country = "", logoUrl = null)
                ),
                tournament = Tournament(id = tournamentId, name = "", location = "", prizePool = 0.0, startDate = "", endDate = "")
            )


            apiService.addMatch(newMatch).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddMatchActivity, "✅ Match ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // 🔽 Montre le message exact d’erreur HTTP (comme 400/500)
                        Toast.makeText(
                            this@AddMatchActivity,
                            "❌ Erreur: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    // 🔽 Affiche l’erreur de réseau (timeout, no route, etc.)
                    Toast.makeText(
                        this@AddMatchActivity,
                        "⛔ Échec réseau: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}
