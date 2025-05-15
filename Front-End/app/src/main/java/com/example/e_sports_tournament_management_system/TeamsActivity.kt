package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.TeamAdapter
import com.example.e_sports_tournament_management_system.model.Team
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TeamsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        recyclerView = findViewById(R.id.recyclerTeams)
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

        findViewById<Button>(R.id.btnAddTeam).setOnClickListener {
            startActivity(Intent(this, AddTeamActivity::class.java))
        }

        fetchTeams()
    }

    override fun onResume() {
        super.onResume()
        fetchTeams()
    }

    private fun fetchTeams() {
        apiService.getTeams().enqueue(object : Callback<List<Team>> {
            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                if (response.isSuccessful) {
                    val teams = response.body() ?: emptyList()
                    recyclerView.adapter = TeamAdapter(teams,
                        onEdit = { team ->
                            val intent = Intent(this@TeamsActivity, EditTeamActivity::class.java)
                            intent.putExtra("TEAM_ID", team.id)
                            intent.putExtra("TEAM_NAME", team.name)
                            intent.putExtra("TEAM_COUNTRY", team.country)
                            intent.putExtra("TEAM_LOGO", team.logoUrl)
                            startActivity(intent)
                        },
                        onDelete = { team ->
                            apiService.deleteTeam(team.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@TeamsActivity, "Équipe supprimée", Toast.LENGTH_SHORT).show()
                                    fetchTeams()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@TeamsActivity, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                Toast.makeText(this@TeamsActivity, "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
