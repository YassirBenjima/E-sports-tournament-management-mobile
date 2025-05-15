package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddMatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)

        val team1Field = findViewById<EditText>(R.id.editTextMatchTeam1)
        val team2Field = findViewById<EditText>(R.id.editTextMatchTeam2)
        val resultField = findViewById<EditText>(R.id.editTextMatchResult)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val team1 = team1Field.text.toString().trim()
            val team2 = team2Field.text.toString().trim()
            val result = resultField.text.toString().trim()

            if (team1.isEmpty() || team2.isEmpty() || result.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newMatch = Match(team1 = team1, team2 = team2, result = result)

            apiService.addMatch(newMatch).enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddMatchActivity, "Match ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddMatchActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    Toast.makeText(this@AddMatchActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
