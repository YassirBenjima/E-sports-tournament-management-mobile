package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.*
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddMatchActivity : AppCompatActivity() {

    private lateinit var spinnerTeam1: Spinner
    private lateinit var spinnerTeam2: Spinner
    private lateinit var spinnerWinner: Spinner
    private var teams: List<Team> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        val tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CrashHandler", "💥 Exception : ${throwable.message}", throwable)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)
        spinnerWinner = findViewById(R.id.spinnerWinner)


        Toast.makeText(this, "🟢 AddMatchActivity ouverte", Toast.LENGTH_SHORT).show()

        spinnerTeam1 = findViewById(R.id.spinnerMatchTeam1)
        spinnerTeam2 = findViewById(R.id.spinnerMatchTeam2)
        val scoreAField = findViewById<EditText>(R.id.editTextScoreA)
        val scoreBField = findViewById<EditText>(R.id.editTextScoreB)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        loadTeams(apiService)

        btnSubmit.setOnClickListener {
            val team1 = teams.getOrNull(spinnerTeam1.selectedItemPosition)
            val team2 = teams.getOrNull(spinnerTeam2.selectedItemPosition)
            val winner = when (spinnerWinner.selectedItemPosition) {
                0 -> team1
                1 -> team2
                else -> null
            }

            if (winner == null) {
                Toast.makeText(this, "Sélectionnez un gagnant", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val scoreA = scoreAField.text.toString().toIntOrNull()
            val scoreB = scoreBField.text.toString().toIntOrNull()

            if (team1 == null || team2 == null || scoreA == null || scoreB == null) {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val newMatch = Match(
                team1 = team1,
                team2 = team2,
                result = MatchResult(teamAScore = scoreA, teamBScore = scoreB, winner = winner),
                tournament = null
            )

            apiService.addMatch(newMatch).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddMatchActivity, "✅ Match ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddMatchActivity,
                            "❌ Erreur: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    Toast.makeText(this@AddMatchActivity, "⛔ Échec réseau: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    private fun updateWinnerSpinner() {
        val selectedTeam1 = teams.getOrNull(spinnerTeam1.selectedItemPosition)
        val selectedTeam2 = teams.getOrNull(spinnerTeam2.selectedItemPosition)

        if (selectedTeam1 != null && selectedTeam2 != null) {
            val names = listOf(selectedTeam1.name, selectedTeam2.name)
            val winnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            winnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerWinner.adapter = winnerAdapter
        }
    }


    private fun loadTeams(apiService: ApiService) {
        apiService.getTeams().enqueue(object : Callback<List<Team>> {
            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                if (response.isSuccessful) {
                    teams = response.body() ?: emptyList()
                    val teamNames = teams.map { it.name }

                    val adapter = ArrayAdapter(this@AddMatchActivity, android.R.layout.simple_spinner_item, teamNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerTeam1.adapter = adapter
                    spinnerTeam2.adapter = adapter

                    // Mettre à jour le spinner du gagnant lorsque l'équipe change
                    spinnerTeam1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            updateWinnerSpinner()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }

                    spinnerTeam2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            updateWinnerSpinner()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }

                    // Initialiser le spinner du gagnant au démarrage
                    updateWinnerSpinner()
                } else {
                    Toast.makeText(this@AddMatchActivity, "Erreur chargement équipes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                Toast.makeText(this@AddMatchActivity, "Erreur réseau équipes", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
