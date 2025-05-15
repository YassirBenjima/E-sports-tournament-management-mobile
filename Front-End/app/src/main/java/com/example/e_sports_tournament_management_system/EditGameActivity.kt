package com.example.e_sports_tournament_management_system


import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditGameActivity : AppCompatActivity() {
    private var gameId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_game)

        val editName = findViewById<EditText>(R.id.editTextGameName)
        val editPlatform = findViewById<EditText>(R.id.editTextPlatform)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        gameId = intent.getLongExtra("GAME_ID", -1)
        editName.setText(intent.getStringExtra("GAME_NAME"))
        editPlatform.setText(intent.getStringExtra("GAME_PLATFORM"))

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnSubmit.text = "Mettre à jour"

        btnSubmit.setOnClickListener {
            val name = editName.text.toString().trim()
            val platform = editPlatform.text.toString().trim()

            if (name.isEmpty() || platform.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedGame = Game(id = gameId, name = name, platform = platform)

            apiService.updateGame(gameId, updatedGame).enqueue(object : Callback<Game> {
                override fun onResponse(call: Call<Game>, response: Response<Game>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditGameActivity, "Jeu mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@EditGameActivity,
                            "Erreur ${response.code()} : $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Game>, t: Throwable) {
                    Toast.makeText(this@EditGameActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnBack.setOnClickListener { finish() }
    }
}
