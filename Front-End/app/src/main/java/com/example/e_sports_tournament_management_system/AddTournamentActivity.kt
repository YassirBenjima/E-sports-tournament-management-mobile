package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddTournamentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tournament)

        val nameField = findViewById<EditText>(R.id.editTextTournamentName)
        val locationField = findViewById<EditText>(R.id.editTextLocation)
        val startDateField = findViewById<EditText>(R.id.editTextStartDate)
        val endDateField = findViewById<EditText>(R.id.editTextEndDate)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val location = locationField.text.toString().trim()
            val startDate = startDateField.text.toString().trim()
            val endDate = endDateField.text.toString().trim()

            if (name.isEmpty() || location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTournament = Tournament(
                name = name,
                location = location,
                startDate = startDate,
                endDate = endDate
            )

            apiService.addTournament(newTournament).enqueue(object : Callback<Tournament> {
                override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddTournamentActivity, "Tournoi ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddTournamentActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Tournament>, t: Throwable) {
                    Toast.makeText(this@AddTournamentActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
