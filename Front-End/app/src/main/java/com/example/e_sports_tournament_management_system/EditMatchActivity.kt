package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.*
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditMatchActivity : AppCompatActivity() {

    private var matchId: Long = -1L
    private lateinit var spinnerTeam1: Spinner
    private lateinit var spinnerTeam2: Spinner
    private lateinit var spinnerWinner: Spinner
    private var teams: List<Team> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_match)

        spinnerTeam1 = findViewById(R.id.spinnerTeam1)
        spinnerTeam2 = findViewById(R.id.spinnerTeam2)
        spinnerWinner = findViewById(R.id.spinnerWinner)
        val scoreAField = findViewById<EditText>(R.id.editTextScoreA)
        val scoreBField = findViewById<EditText>(R.id.editTextScoreB)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        matchId = intent.getLongExtra("MATCH_ID", -1)
        val team1Name = intent.getStringExtra("TEAM1_NAME") ?: ""
        val team2Name = intent.getStringExtra("TEAM2_NAME") ?: ""
        val teamAScore = intent.getIntExtra("TEAM1_SCORE", 0)
        val teamBScore = intent.getIntExtra("TEAM2_SCORE", 0)
        val winnerName = intent.getStringExtra("WINNER_NAME") ?: ""

        scoreAField.setText(teamAScore.toString())
        scoreBField.setText(teamBScore.toString())
        btnSubmit.text = "Mettre à jour"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        loadTeams(apiService, team1Name, team2Name, winnerName)

        btnSubmit.setOnClickListener {
            val selectedTeam1 = teams.getOrNull(spinnerTeam1.selectedItemPosition)
            val selectedTeam2 = teams.getOrNull(spinnerTeam2.selectedItemPosition)
            val selectedWinner = teams.getOrNull(spinnerWinner.selectedItemPosition)
            val scoreA = scoreAField.text.toString().toIntOrNull()
            val scoreB = scoreBField.text.toString().toIntOrNull()

            if (selectedTeam1 == null || selectedTeam2 == null || selectedWinner == null || scoreA == null || scoreB == null) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedMatch = Match(
                id = matchId,
                team1 = selectedTeam1,
                team2 = selectedTeam2,
                result = MatchResult(
                    teamAScore = scoreA,
                    teamBScore = scoreB,
                    winner = selectedWinner
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

    private fun loadTeams(apiService: ApiService, team1Name: String, team2Name: String, winnerName: String) {
        apiService.getTeams().enqueue(object : Callback<List<Team>> {
            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                if (response.isSuccessful) {
                    teams = response.body() ?: emptyList()
                    val teamNames = teams.map { it.name }

                    val adapter = ArrayAdapter(this@EditMatchActivity, android.R.layout.simple_spinner_item, teamNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerTeam1.adapter = adapter
                    spinnerTeam2.adapter = adapter
                    spinnerWinner.adapter = adapter

                    spinnerTeam1.setSelection(teamNames.indexOf(team1Name))
                    spinnerTeam2.setSelection(teamNames.indexOf(team2Name))
                    spinnerWinner.setSelection(teamNames.indexOf(winnerName))
                }
            }

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                Toast.makeText(this@EditMatchActivity, "Erreur chargement équipes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
