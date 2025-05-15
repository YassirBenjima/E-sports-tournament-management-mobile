package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.PlayerAdapter
import com.example.e_sports_tournament_management_system.model.Player
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PlayersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

        recyclerView = findViewById(R.id.recyclerPlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        findViewById<ImageButton>(R.id.btnBackDashboard).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnAddPlayer).setOnClickListener {
            startActivity(Intent(this, AddPlayerActivity::class.java))
        }

        fetchPlayers()
    }

    override fun onResume() {
        super.onResume()
        fetchPlayers()
    }

    private fun fetchPlayers() {
        apiService.getPlayers().enqueue(object : Callback<List<Player>> {
            override fun onResponse(call: Call<List<Player>>, response: Response<List<Player>>) {
                if (response.isSuccessful) {
                    val players = response.body() ?: emptyList()
                    recyclerView.adapter = PlayerAdapter(players,
                        onEdit = { player ->
                            val intent = Intent(this@PlayersActivity, EditPlayerActivity::class.java)
                            intent.putExtra("PLAYER_ID", player.id)
                            intent.putExtra("PLAYER_NAME", player.username)
                            intent.putExtra("PLAYER_AGE", player.age.toString())
                            intent.putExtra("PLAYER_NATIONALITY", player.nationality)
                            startActivity(intent)
                        },
                        onDelete = { player ->
                            apiService.deletePlayer(player.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@PlayersActivity, "Joueur supprimé", Toast.LENGTH_SHORT).show()
                                    fetchPlayers()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@PlayersActivity, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<Player>>, t: Throwable) {
                Toast.makeText(this@PlayersActivity, "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
