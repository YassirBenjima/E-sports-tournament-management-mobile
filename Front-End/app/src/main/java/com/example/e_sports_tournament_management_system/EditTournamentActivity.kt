package com.example.e_sports_tournament_management_system

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class EditTournamentActivity : AppCompatActivity() {
    private var tournamentId: Long = -1L
    private lateinit var apiService: ApiService
    private lateinit var gameSpinner: Spinner
    private var gamesList: List<Game> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tournament)

        val nameField = findViewById<EditText>(R.id.editTextTournamentName)
        val locationField = findViewById<EditText>(R.id.editTextLocation)
        val startDateField = findViewById<EditText>(R.id.editTextStartDate)
        val endDateField = findViewById<EditText>(R.id.editTextEndDate)
        val prizePoolField = findViewById<EditText>(R.id.editTextPrizePool)
        gameSpinner = findViewById(R.id.spinnerGames)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        tournamentId = intent.getLongExtra("TOURNAMENT_ID", -1)
        nameField.setText(intent.getStringExtra("TOURNAMENT_NAME"))
        locationField.setText(intent.getStringExtra("TOURNAMENT_LOCATION"))
        startDateField.setText(intent.getStringExtra("TOURNAMENT_START_DATE"))
        endDateField.setText(intent.getStringExtra("TOURNAMENT_END_DATE"))
        prizePoolField.setText(intent.getDoubleExtra("TOURNAMENT_PRIZE", 0.0).toString())

        startDateField.setOnClickListener { showDatePickerDialog { startDateField.setText(it) } }
        endDateField.setOnClickListener { showDatePickerDialog { endDateField.setText(it) } }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        loadGames {
            val selectedGameName = intent.getStringExtra("TOURNAMENT_GAME")
            val selectedIndex = gamesList.indexOfFirst { it.name == selectedGameName }
            if (selectedIndex >= 0) gameSpinner.setSelection(selectedIndex)
        }

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val location = locationField.text.toString().trim()
            val startDate = startDateField.text.toString().trim()
            val endDate = endDateField.text.toString().trim()
            val prizePool = prizePoolField.text.toString().toDoubleOrNull()

            if (name.isEmpty() || location.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || prizePool == null) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedGame = gamesList.getOrNull(gameSpinner.selectedItemPosition)
            if (selectedGame == null) {
                Toast.makeText(this, "Aucun jeu sélectionné", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTournament = Tournament(
                id = tournamentId,
                name = name,
                location = location,
                startDate = startDate,
                endDate = endDate,
                prizePool = prizePool,
                game = selectedGame
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

    private fun loadGames(onLoaded: () -> Unit) {
        apiService.getGames().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    gamesList = response.body() ?: emptyList()
                    val gameNames = gamesList.map { it.name }
                    val adapter = ArrayAdapter(this@EditTournamentActivity, android.R.layout.simple_spinner_item, gameNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    gameSpinner.adapter = adapter
                    onLoaded()
                } else {
                    Toast.makeText(this@EditTournamentActivity, "Erreur chargement jeux", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(this@EditTournamentActivity, "Échec réseau: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            val date = String.format("%04d-%02d-%02d", y, m + 1, d)
            onDateSelected(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
