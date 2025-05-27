package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TournamentDetailsActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private var tournamentId: Long = -1
    private lateinit var title: TextView
    private lateinit var btnAddMatch: Button
    private lateinit var layoutBrackets: LinearLayout
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)
        if (tournamentId == -1L) {
            Toast.makeText(this, "ID de tournoi manquant", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        title = findViewById(R.id.textTournamentTitle)
        btnAddMatch = findViewById(R.id.btnAddMatchToTournament)
        layoutBrackets = findViewById(R.id.layoutBrackets)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        btnAddMatch.setOnClickListener {
            val intent = Intent(this, AddMatchActivity::class.java)
            intent.putExtra("TOURNAMENT_ID", tournamentId)
            startActivity(intent)
        }

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
        layoutBrackets.removeAllViews()

        fun addSectionTitle(title: String) {
            val titleView = TextView(this).apply {
                text = "🔻 $title"
                setTextColor(resources.getColor(android.R.color.holo_red_light))
                setTypeface(null, android.graphics.Typeface.BOLD)
                textSize = 18f
                setPadding(0, 16, 0, 8)
            }
            layoutBrackets.addView(titleView)
        }

        val matchCount = matches.size

        if (matchCount >= 1) {
            addSectionTitle("Quarts de finale")
            matches.take(4).forEachIndexed { index, match ->
                layoutBrackets.addView(createMatchView(index + 1, match))
            }
        }

        if (matchCount >= 5) {
            addSectionTitle("Demi-finales")
            matches.drop(4).take(2).forEachIndexed { index, match ->
                layoutBrackets.addView(createMatchView(index + 5, match))
            }
        }

        if (matchCount >= 7) {
            addSectionTitle("Finale")
            layoutBrackets.addView(createMatchView(7, matches[6]))

            val winnerName = matches[6].result?.winner?.name ?: "Inconnu"
            val winnerView = TextView(this).apply {
                text = "🏆 Vainqueur du tournoi : $winnerName 🏆"
                setTextColor(resources.getColor(android.R.color.holo_orange_light))
                setTypeface(null, android.graphics.Typeface.BOLD)
                textSize = 20f
                setPadding(0, 16, 0, 16)
            }
            layoutBrackets.addView(winnerView)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchTournamentDetails()
    }


    private fun createMatchView(index: Int, match: Match): TextView {
        val team1 = match.team1?.name ?: "Équipe A"
        val team2 = match.team2?.name ?: "Équipe B"
        val score = if (match.result != null) "${match.result.teamAScore} - ${match.result.teamBScore}" else "-"
        val winner = match.result?.winner?.name ?: "Indéfini"

        return TextView(this).apply {
            text = "Match $index: $team1 vs $team2 | Score: $score | 🏆 $winner"
            setTextColor(resources.getColor(android.R.color.white))
            setPadding(8, 4, 8, 4)
        }
    }

}
