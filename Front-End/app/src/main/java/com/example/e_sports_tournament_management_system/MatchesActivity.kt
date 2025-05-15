package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.MatchAdapter
import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MatchesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)

        recyclerView = findViewById(R.id.recyclerMatches)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        findViewById<ImageButton>(R.id.btnBackDashboard).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnAddMatch).setOnClickListener {
            startActivity(Intent(this, AddMatchActivity::class.java))
        }

        fetchMatches()
    }

    override fun onResume() {
        super.onResume()
        fetchMatches()
    }

    private fun fetchMatches() {
        apiService.getMatches().enqueue(object : Callback<List<Match>> {
            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                if (response.isSuccessful) {
                    val matches = response.body() ?: emptyList()
                    recyclerView.adapter = MatchAdapter(matches,
                        onEdit = { match ->
                            val intent = Intent(this@MatchesActivity, EditMatchActivity::class.java)
                            intent.putExtra("MATCH_ID", match.id)
                            intent.putExtra("MATCH_TEAM1", match.team1)
                            intent.putExtra("MATCH_TEAM2", match.team2)
                            intent.putExtra("MATCH_RESULT", match.result)
                            startActivity(intent)
                        },
                        onDelete = { match ->
                            apiService.deleteMatch(match.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@MatchesActivity, "Match supprimé", Toast.LENGTH_SHORT).show()
                                    fetchMatches()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@MatchesActivity, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                Toast.makeText(this@MatchesActivity, "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
