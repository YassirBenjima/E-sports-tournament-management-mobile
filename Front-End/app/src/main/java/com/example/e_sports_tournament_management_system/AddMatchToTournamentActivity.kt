package com.example.e_sports_tournament_management_system

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.*
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddMatchToTournamentActivity : AppCompatActivity() {

    private var tournamentId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match_to_tournament)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val team1Field = findViewById<EditText>(R.id.editTextTeam1)
        val team2Field = findViewById<EditText>(R.id.editTextTeam2)
        val scoreAField = findViewById<EditText>(R.id.editTextScoreA)
        val scoreBField = findViewById<EditText>(R.id.editTextScoreB)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val team1Name = team1Field.text.toString().trim()
            val team2Name = team2Field.text.toString().trim()
            val scoreA = scoreAField.text.toString().toIntOrNull()
            val scoreB = scoreBField.text.toString().toIntOrNull()

            if (team1Name.isEmpty() || team2Name.isEmpty() || scoreA == null || scoreB == null) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val team1 = Team(name = team1Name, country = "", logoUrl = null)
            val team2 = Team(name = team2Name, country = "", logoUrl = null)
            val winner = if (scoreA > scoreB) team1 else team2

            val result = MatchResult(teamAScore = scoreA, teamBScore = scoreB, winner = winner)
            val match = Match(team1 = team1, team2 = team2, result = result)

            apiService.addMatchToTournament(tournamentId, match).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddMatchToTournamentActivity, "Match ajouté", Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        intent.putExtra("REFRESH", true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this@AddMatchToTournamentActivity, "Erreur : ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    Toast.makeText(this@AddMatchToTournamentActivity, "Échec : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
