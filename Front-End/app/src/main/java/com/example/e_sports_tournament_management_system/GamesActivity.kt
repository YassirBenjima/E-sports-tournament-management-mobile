package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.GameAdapter
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class GamesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        recyclerView = findViewById(R.id.recyclerGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        findViewById<ImageButton>(R.id.btnBackDashboard).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnAddGame).setOnClickListener {
            startActivity(Intent(this, AddGameActivity::class.java))
        }

        fetchGames()
    }

    override fun onResume() {
        super.onResume()
        fetchGames()
    }

    private fun fetchGames() {
        apiService.getGames().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    recyclerView.adapter = GameAdapter(games,
                        onEdit = { game ->
                            if (game.id == null) {
                                Toast.makeText(this@GamesActivity, "ID du jeu invalide", Toast.LENGTH_SHORT).show()

                            }
                            val intent = Intent(this@GamesActivity, EditGameActivity::class.java)
                            intent.putExtra("GAME_ID", game.id)
                            intent.putExtra("GAME_NAME", game.name)
                            intent.putExtra("GAME_PLATFORM", game.platform)
                            startActivity(intent)
                        }
                        ,
                        onDelete = { game ->
                            apiService.deleteGame(game.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@GamesActivity, "Jeu supprimé", Toast.LENGTH_SHORT).show()
                                    fetchGames()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@GamesActivity, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(this@GamesActivity, "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
