package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.TournamentAdapter
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TournamentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournaments)

        recyclerView = findViewById(R.id.recyclerTournaments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        findViewById<ImageButton>(R.id.btnBackDashboard).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnAddTournament).setOnClickListener {
            startActivity(Intent(this, AddTournamentActivity::class.java))
        }

        fetchTournaments()
    }

    override fun onResume() {
        super.onResume()
        fetchTournaments()
    }

    private fun fetchTournaments() {
        apiService.getTournaments().enqueue(object : Callback<List<Tournament>> {
            override fun onResponse(call: Call<List<Tournament>>, response: Response<List<Tournament>>) {
                if (response.isSuccessful) {
                    val tournaments = response.body() ?: emptyList()
                    recyclerView.adapter = TournamentAdapter(tournaments,
                        onClick = { tournament ->
                            val intent = Intent(this@TournamentsActivity, TournamentDetailsActivity::class.java)
                            intent.putExtra("TOURNAMENT_ID", tournament.id)
                            startActivity(intent)
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<Tournament>>, t: Throwable) {
                Toast.makeText(this@TournamentsActivity, "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
