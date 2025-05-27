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

    private lateinit var spinnerTeam1: Spinner
    private lateinit var spinnerTeam2: Spinner
    private var teams: List<Team> = emptyList()


    private var tournamentId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match_to_tournament)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        spinnerTeam1 = findViewById(R.id.spinnerTeam1)
        spinnerTeam2 = findViewById(R.id.spinnerTeam2)
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
            val team1 = teams.getOrNull(spinnerTeam1.selectedItemPosition)
            val team2 = teams.getOrNull(spinnerTeam2.selectedItemPosition)
            val scoreA = scoreAField.text.toString().toIntOrNull()
            val scoreB = scoreBField.text.toString().toIntOrNull()

            if (team1 == null || team2 == null || scoreA == null || scoreB == null) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val winner = if (scoreA > scoreB) team1 else team2

            val result = MatchResult(teamAScore = scoreA, teamBScore = scoreB, winner = winner)
            val match = Match(
                team1 = team1,
                team2 = team2,
                result = result,
                tournament = Tournament(id = tournamentId, name = "", location = "", prizePool = 0.00, startDate = "", endDate = "")
            )

            apiService.addMatchToTournament(tournamentId, match).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddMatchToTournamentActivity, "Match ajouté", Toast.LENGTH_SHORT).show()
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
    private fun loadTeams(apiService: ApiService) {
        apiService.getTeams().enqueue(object : Callback<List<Team>> {
            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                if (response.isSuccessful) {
                    teams = response.body() ?: emptyList()
                    val teamNames = teams.map { it.name }

                    val adapter = ArrayAdapter(this@AddMatchToTournamentActivity, android.R.layout.simple_spinner_item, teamNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTeam1.adapter = adapter
                    spinnerTeam2.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                Toast.makeText(this@AddMatchToTournamentActivity, "Erreur chargement équipes", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
