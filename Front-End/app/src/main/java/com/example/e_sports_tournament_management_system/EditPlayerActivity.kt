package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Player
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditPlayerActivity : AppCompatActivity() {

    private var playerId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_player)

        val usernameField = findViewById<EditText>(R.id.editTextPlayerName)
        val ageField = findViewById<EditText>(R.id.editTextPlayerAge) // anciennement Email
        val nationalityField = findViewById<EditText>(R.id.editTextPlayerNationality)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        playerId = intent.getLongExtra("PLAYER_ID", -1)
        usernameField.setText(intent.getStringExtra("PLAYER_NAME"))
        ageField.setText(intent.getStringExtra("PLAYER_AGE"))
        nationalityField.setText(intent.getStringExtra("PLAYER_NATIONALITY"))

        btnSubmit.text = "Mettre à jour"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnSubmit.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val ageText = ageField.text.toString().trim()
            val nationality = nationalityField.text.toString().trim()

            if (username.isEmpty() || ageText.isEmpty() || nationality.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null) {
                Toast.makeText(this, "L'âge doit être un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedPlayer = Player(
                id = playerId,
                username = username,
                age = age,
                nationality = nationality,
                imageUrl = null
            )

            apiService.updatePlayer(playerId, updatedPlayer).enqueue(object : Callback<Player> {
                override fun onResponse(call: Call<Player>, response: Response<Player>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditPlayerActivity, "Joueur mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@EditPlayerActivity,
                            "Erreur ${response.code()} : $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Player>, t: Throwable) {
                    Toast.makeText(this@EditPlayerActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnBack.setOnClickListener { finish() }
    }
}
