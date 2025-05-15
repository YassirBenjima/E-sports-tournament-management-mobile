package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Player
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val usernameField = findViewById<EditText>(R.id.editTextPlayerName)
        val ageField = findViewById<EditText>(R.id.editTextPlayerAge) // ancien ID utilisé pour l'email → remapper en XML
        val nationalityField = findViewById<EditText>(R.id.editTextPlayerNationality)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val ageText = ageField.text.toString().trim()
            val nationality = nationalityField.text.toString().trim()

            if (username.isEmpty() || ageText.isEmpty() || nationality.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null) {
                Toast.makeText(this, "L'âge doit être un nombre valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPlayer = Player(
                username = username,
                age = age,
                nationality = nationality,
                imageUrl = null
            )

            apiService.addPlayer(newPlayer).enqueue(object : Callback<Player> {
                override fun onResponse(call: Call<Player>, response: Response<Player>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddPlayerActivity, "Joueur ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddPlayerActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Player>, t: Throwable) {
                    Toast.makeText(this@AddPlayerActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
