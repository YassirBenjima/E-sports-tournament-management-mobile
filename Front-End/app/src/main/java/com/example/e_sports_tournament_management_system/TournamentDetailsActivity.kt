package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TournamentDetailsActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var layoutBrackets: LinearLayout
    private lateinit var title: TextView
    private var tournamentId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        layoutBrackets = findViewById(R.id.layoutBrackets)
        title = findViewById(R.id.textTournamentTitle)

        tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        fetchTournamentDetails()
    }

    private fun fetchTournamentDetails() {
        apiService.getTournamentById(tournamentId).enqueue(object : Callback<Tournament> {
            override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                if (response.isSuccessful) {
                    val tournament = response.body()
                    tournament?.let {
                        title.text = "Tournoi: ${it.name}"
                        displayBrackets(it.matches ?: emptyList())
                    }
                } else {
                    Toast.makeText(this@TournamentDetailsActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                Toast.makeText(this@TournamentDetailsActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayBrackets(matches: List<Match>) {
        val grouped = matches.groupBy {
            when (it.result?.teamAScore ?: 0 + (it.result?.teamBScore ?: 0)) {
                in 0..3 -> "Quarts de finale"
                in 4..5 -> "Demi-finales"
                else -> "Finale"
            }
        }

        val order = listOf("Quarts de finale", "Demi-finales", "Finale")

        for (round in order) {
            val roundMatches = grouped[round] ?: continue
            val titleView = TextView(this).apply {
                text = "🎯 $round"
                textSize = 18f
                setTextColor(resources.getColor(android.R.color.white, null))
            }
            layoutBrackets.addView(titleView)

            for (match in roundMatches) {
                val view = TextView(this).apply {
                    text = "⚔️ ${match.team1.name} ${match.result?.teamAScore ?: "?"} - ${match.result?.teamBScore ?: "?"} ${match.team2.name} \n🏆 Gagnant : ${match.result?.winner?.name ?: "?"}"
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    setPadding(0, 8, 0, 8)
                }
                layoutBrackets.addView(view)
            }
        }
    }
}
