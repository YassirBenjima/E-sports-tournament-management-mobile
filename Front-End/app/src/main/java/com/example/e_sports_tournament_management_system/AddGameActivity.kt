package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddGameActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        val editTextName = findViewById<EditText>(R.id.editTextGameName)
        val editTextPlatform = findViewById<EditText>(R.id.editTextPlatform)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        // Retrofit
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // Remplace par IP locale si besoin
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Bouton retour
        btnBack.setOnClickListener {
            finish()
        }

        // Bouton soumettre
        btnSubmit.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val platform = editTextPlatform.text.toString().trim()

            if (name.isEmpty() || platform.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val game = Game(name = name, platform = platform)

            apiService.addGame(game).enqueue(object : Callback<Game> {
                override fun onResponse(call: Call<Game>, response: Response<Game>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddGameActivity, "Jeu ajouté avec succès", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddGameActivity, "Erreur: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Game>, t: Throwable) {
                    Toast.makeText(this@AddGameActivity, "Échec : ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
