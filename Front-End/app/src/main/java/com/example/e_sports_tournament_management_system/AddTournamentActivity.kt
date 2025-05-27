package com.example.e_sports_tournament_management_system

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.model.Tournament
import com.example.e_sports_tournament_management_system.network.ApiService
import com.google.gson.Gson
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AddTournamentActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var spinnerGame: Spinner
    private var gameList: List<Game> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tournament)

        val nameField = findViewById<EditText>(R.id.editTextTournamentName)
        val locationField = findViewById<EditText>(R.id.editTextLocation)
        val startDateField = findViewById<EditText>(R.id.editTextStartDate)
        val endDateField = findViewById<EditText>(R.id.editTextEndDate)
        val prizePoolField = findViewById<EditText>(R.id.editTextPrizePool)
        spinnerGame = findViewById(R.id.spinnerGame)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        startDateField.setOnClickListener { showDatePickerDialog { startDateField.setText(it) } }
        endDateField.setOnClickListener { showDatePickerDialog { endDateField.setText(it) } }

        loadGames()

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val location = locationField.text.toString().trim()
            val startDate = startDateField.text.toString().trim()
            val endDate = endDateField.text.toString().trim()
            val prizePoolInput = prizePoolField.text.toString().trim()

            if (name.isEmpty() || location.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || prizePoolInput.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prizePool = try {
                prizePoolInput.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Montant invalide pour le prize pool", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedGame = gameList.getOrNull(spinnerGame.selectedItemPosition)
            if (selectedGame == null) {
                Toast.makeText(this, "Aucun jeu sélectionné", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTournament = Tournament(
                name = name,
                location = location,
                startDate = startDate,
                endDate = endDate,
                prizePool = prizePool,
                game = selectedGame
            )
            val json = Gson().toJson(newTournament)
            Log.d("TournamentJSON", json)


            apiService.addTournament(newTournament).enqueue(object : Callback<Tournament> {
                override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddTournamentActivity, "Tournoi ajouté", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddTournamentActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Tournament>, t: Throwable) {
                    Toast.makeText(this@AddTournamentActivity, "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AddTournament", "Erreur réseau", t)
                }
            })
        }
    }

    private fun loadGames() {
        apiService.getGames().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    gameList = response.body() ?: emptyList()
                    val gameNames = gameList.map { it.name }
                    spinnerGame.adapter = ArrayAdapter(this@AddTournamentActivity, android.R.layout.simple_spinner_dropdown_item, gameNames)
                } else {
                    Toast.makeText(this@AddTournamentActivity, "Erreur chargement jeux", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(this@AddTournamentActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_SHORT).show()
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
