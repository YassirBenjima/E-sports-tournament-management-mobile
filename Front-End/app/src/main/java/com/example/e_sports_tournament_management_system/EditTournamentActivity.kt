package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditTournamentActivity : AppCompatActivity() {
    private var tournamentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tournament)

        val nameField = findViewById<EditText>(R.id.editTextTournamentName)
        val locationField = findViewById<EditText>(R.id.editTextLocation)
        val startDateField = findViewById<EditText>(R.id.editTextStartDate)
        val endDateField = findViewById<EditText>(R.id.editTextEndDate)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)
        nameField.setText(intent.getStringExtra("TOURNAMENT_NAME"))
        locationField.setText(intent.getStringExtra("TOURNAMENT_LOCATION"))
        startDateField.setText(intent.getStringExtra("TOURNAMENT_START"))
        endDateField.setText(intent.getStringExtra("TOURNAMENT_END"))

        btnSubmit.text = "Mettre à jour"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val location = locationField.text.toString().trim()
            val startDate = startDateField.text.toString().trim()
            val endDate = endDateField.text.toString().trim()

            if (name.isEmpty() || location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTournament = Tournament(
                id = tournamentId,
                name = name,
                location = location,
                startDate = startDate,
                endDate = endDate
            )

            apiService.updateTournament(tournamentId, updatedTournament).enqueue(object : Callback<Tournament> {
                override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditTournamentActivity, "Tournoi mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditTournamentActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Tournament>, t: Throwable) {
                    Toast.makeText(this@EditTournamentActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnBack.setOnClickListener { finish() }
    }
}
