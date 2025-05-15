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

        // Retrofit setup
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // Remplace si besoin par l’IP locale ou ngrok
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Retour vers le dashboard
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)
        btnBack.setOnClickListener {
            finish()
        }

        // Aller vers la page d'ajout de jeu
        val btnAddGame = findViewById<Button>(R.id.btnAddGame)
        btnAddGame.setOnClickListener {
            val intent = Intent(this@GamesActivity, AddGameActivity::class.java)
            startActivity(intent)
        }

        fetchGames()
    }

    private fun fetchGames() {
        apiService.getGames().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    recyclerView.adapter = GameAdapter(games)
                } else {
                    Toast.makeText(this@GamesActivity, "Erreur: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(this@GamesActivity, "Échec : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
